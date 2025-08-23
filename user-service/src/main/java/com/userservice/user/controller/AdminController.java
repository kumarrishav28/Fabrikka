package com.userservice.user.controller;

import com.fabrikka.common.InventoryDto;
import com.fabrikka.common.ProductDto;
import com.userservice.user.config.InventoryClient;
import com.userservice.user.config.LoadProductClient;
import com.userservice.user.config.ProductClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final ProductClient productClient;

    private final InventoryClient inventoryClient;

    private final LoadProductClient loadProductClient;

    @GetMapping("/admin-dashboard")
    public String adminDashboard() {
        return "admin-dashboard";
    }

    @GetMapping("/add-product")
    public String addProductForm() {
        return "add-product";
    }

    @PostMapping("/add-product")
    public String addProduct(@ModelAttribute ProductDto productDto, RedirectAttributes redirectAttributes) {
        try {
            ResponseEntity<String> response = productClient.addProduct(productDto);
            if (response.getStatusCode().is2xxSuccessful()) {
                redirectAttributes.addFlashAttribute("successMessage", "Product has been added successfully!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Failed to add product. Service returned: " + response.getStatusCode());
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error communicating with Product Service.");
        }
        return "redirect:/admin-dashboard";
    }

    @GetMapping("/add-inventory")
    public String addInventoryForm() {
        return "add-inventory";
    }

    @GetMapping("/products")
    public String viewProducts(Model model) {
        try {
            ResponseEntity<List<ProductDto>> response = productClient.getAllProducts();
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                model.addAttribute("productDtoList", response.getBody());
            } else {
                model.addAttribute("errorMessage", "Could not retrieve products. Service returned: " + response.getStatusCode());
                model.addAttribute("productDtoList", Collections.emptyList());
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error communicating with Product Service.");
            model.addAttribute("productDtoList", Collections.emptyList());
        }
        return "product-list";
    }

    @PostMapping("/add-inventory")
    public String addInventory(@ModelAttribute InventoryDto inventoryDto, RedirectAttributes redirectAttributes) {
        try {
            ResponseEntity<String> response = inventoryClient.updateInventory(inventoryDto.getProductId(), inventoryDto.getAvailableStock());
            if (response.getStatusCode().is2xxSuccessful()) {
                redirectAttributes.addFlashAttribute("successMessage", "Inventory has been updated successfully!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Failed to update inventory. Service returned: " + response.getStatusCode());
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error communicating with Inventory Service.");
        }
        return "redirect:/admin-dashboard";
    }

    @GetMapping("/inventory")
    public String viewInventory(@RequestParam(value = "productId",required = false) String productId , Model model) {
        if (productId != null && !productId.isEmpty()) {
            try {
                ResponseEntity<InventoryDto> response = inventoryClient.getInventory(UUID.fromString(productId));
                if (response.getStatusCode().is2xxSuccessful()) {
                    model.addAttribute("inventory", response.getBody());
                }
            } catch (Exception e) {
                model.addAttribute("errorMessage", "Error communicating with Inventory Service.");
            }
        }
        return "inventory";
    }

    @PostMapping("/add-product-bulk")
    public String addProductInBulk(@RequestParam MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file == null || file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "File is empty");
            return "redirect:/add-product-bulk";
        }
        try {
            loadProductClient.uploadProductFile(file);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "There was an error processing your file. Please check the file format and try again.");
            return "redirect:/add-product-bulk";
        }
        redirectAttributes.addFlashAttribute("successMessage", "Your file is being processed. You will receive a confirmation email once the process is complete.");
        return "redirect:/admin-dashboard";
    }

    @GetMapping("/add-product-bulk")
    public String addProductInBulkForm() {
        return "add-product-bulk";
    }
}