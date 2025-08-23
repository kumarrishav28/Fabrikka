package com.fabrikka.product_service.service.impl;

import com.fabrikka.common.CategoryDto;
import com.fabrikka.common.InventoryDto;
import com.fabrikka.common.ProductDto;
import com.fabrikka.product_service.config.InventoryClient;
import com.fabrikka.product_service.entity.Category;
import com.fabrikka.product_service.entity.Product;
import com.fabrikka.product_service.repository.CategoryRepository;
import com.fabrikka.product_service.repository.ProductRepository;
import com.fabrikka.product_service.service.ProductService;
import com.fabrikka.product_service.service.specification.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    final ProductRepository productRepository;

    final CategoryRepository categoryRepository;

    final InventoryClient inventoryClient;


    @Override
    public void createProduct(ProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        Category category = new Category();
        category.setName(productDto.getCategory().getName());
        product.setCategory(category);
        product.setImageUrl(productDto.getImageUrl());
        product = productRepository.save(product);
        InventoryDto inventory =  productDto.getInventory();
        inventory.setProductId(product.getProductId());
        createInventory(inventory);

    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::convertToProductDto).collect(Collectors.toList());
    }

    @Override
    public ProductDto getProductById(UUID id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        return convertToProductDto(product);
    }

    @Override
    public void deleteProduct(UUID id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);
    }

    @Override
    public void updateProduct(UUID id, ProductDto productDto) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setImageUrl(productDto.getImageUrl());
        Category category = new Category();
        category.setName(productDto.getCategory().getName());
        product.setCategory(category);
        productRepository.save(product);
        inventoryClient.updateInventory(product.getProductId(), productDto.getInventory().getAvailableStock());
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> new CategoryDto(category.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public void saveAll(List<ProductDto> productDtos) {
        // Map product name to ProductDto for quick lookup
        Map<String, ProductDto> dtoMap = productDtos.stream()
                .collect(Collectors.toMap(ProductDto::getName, dto -> dto));

        List<Product> products = productDtos.stream().map(dto -> {
            Product product = new Product();
            product.setName(dto.getName());
            product.setDescription(dto.getDescription());
            product.setPrice(dto.getPrice());
            product.setImageUrl(dto.getImageUrl());
            // Reuse existing categories or create new ones to avoid duplicates
            Category category = categoryRepository.findByName(dto.getCategory().getName())
                    .orElse(new Category(dto.getCategory().getName()));
            product.setCategory(category);
            return product;
        }).collect(Collectors.toList());

        List<Product> savedProducts = productRepository.saveAll(products);

        // To improve performance for this bulk operation, processing inventory creation in parallel.
        savedProducts.parallelStream().forEach(product -> {
            ProductDto dto = dtoMap.get(product.getName());
            InventoryDto inventory = new InventoryDto();
            inventory.setProductId(product.getProductId());
            inventory.setAvailableStock(dto.getInventory().getAvailableStock());
            createInventory(inventory);
        });
    }

    @Override
    public Page<ProductDto> getProductsPaginated(int page, int size, List<String> categories, Double minPrice, Double maxPrice, String sort) {
        // 1. Build Pageable with sorting
        Pageable pageable = createPageable(page, size, sort);

        // 2. Build the dynamic query Specification
        Specification<Product> spec = buildSpecification(categories, minPrice, maxPrice);

        // 3. Query the repository using the specification and pageable
        Page<Product> productPage = productRepository.findAll(spec, pageable);

        // 4. Map Page<Product> to Page<ProductDto>
        // The .map() function on a Page object is the ideal way to convert content
        // while preserving pagination information (total pages, size, etc.).
        return productPage.map(this::convertToProductDto);
    }

    private Specification<Product> buildSpecification(List<String> categories, Double minPrice, Double maxPrice) {

        Specification<Product> spec = Specification.where(null);

        if (categories != null && !categories.isEmpty()) {
            spec = spec.and(ProductSpecification.hasCategoryIn(categories));
        }
        if (minPrice != null) {
            spec = spec.and(ProductSpecification.isGreaterThanOrEqualToPrice(minPrice));
        }
        if (maxPrice != null) {
            spec = spec.and(ProductSpecification.isLessThanOrEqualToPrice(maxPrice));
        }
        return spec;
    }

    private Pageable createPageable(int page, int size, String sort) {
        Sort sortOrder = Sort.unsorted();
        if (sort != null && !sort.isEmpty()) {
            try {
                String[] sortParams = sort.split(",");
                if (sortParams.length == 2) {
                    String property = sortParams[0];
                    Sort.Direction direction = Sort.Direction.fromString(sortParams[1]);
                    sortOrder = Sort.by(direction, property);
                }
            } catch (IllegalArgumentException e) {
                logger.error("Invalid sort parameter:{}" , sort);
            }
        }
        return PageRequest.of(page, size, sortOrder);
    }

    private ProductDto convertToProductDto(Product product) {
        // NOTE: This results in N+1 network calls (1 for the page + N for inventories).
        // A future optimization would be to fetch all required inventories in a single bulk call.
        InventoryDto inventory;
        try {
            inventory = inventoryClient.getInventory(product.getProductId()).getBody();
        } catch (Exception e) {
            logger.error("Could not fetch inventory for product :{},exception:{} " , product.getProductId() , e.getMessage());
            inventory = new InventoryDto(product.getProductId(), 0);
        }
        return new ProductDto(
                product.getName(), product.getDescription(), product.getPrice(),
                product.getImageUrl(), new CategoryDto(product.getCategory().getName()),
                inventory, product.getProductId()
        );
    }

    public void createInventory(InventoryDto inventoryDto) {
        inventoryClient.addInventory(inventoryDto);
    }

}
