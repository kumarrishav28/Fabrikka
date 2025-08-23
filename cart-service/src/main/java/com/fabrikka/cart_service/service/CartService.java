package com.fabrikka.cart_service.service;

import com.fabrikka.cart_service.entity.Cart;
import com.fabrikka.cart_service.entity.CartItem;
import com.fabrikka.cart_service.exception.ResourceNotFoundException;
import com.fabrikka.cart_service.repository.CartItemRepository;
import com.fabrikka.cart_service.repository.CartRepository;
import com.fabrikka.common.CartDto;
import com.fabrikka.common.CartItemDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartMapper cartMapper;

    Logger logger = LoggerFactory.getLogger(CartService.class);

    public CartDto createCart(Long userId) {
        Cart cart = Cart.builder().userId(userId).build();
        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toDto(savedCart);
    }

    public CartDto getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .map(cartMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "userId", userId.toString()));
    }

    public CartDto addItemToCart(Long userId, UUID productId, Integer quantity) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> cartMapper.toEntity(createCart(userId)));

        // Check if item for this product already exists. If so, update quantity.
        Optional<CartItem> existingItemOpt = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            // Otherwise, add a new item using the helper method.
            CartItem newItem = CartItem.builder()
                    .productId(productId)
                    .quantity(quantity)
                    .build();
            cart.addItem(newItem);
        }

        Cart updatedCart = cartRepository.save(cart);
        return cartMapper.toDto(updatedCart);
    }

    public CartDto updateItemQuantity(Long userId, Long itemId, Integer newQuantity) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "userId", userId.toString()));

        CartItem item = cart.getItems()
                .stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", itemId.toString()));

        item.setQuantity(newQuantity);
        Cart updatedCart = cartRepository.save(cart);
        return cartMapper.toDto(updatedCart);
    }

    public CartDto removeItemFromCart(Long userId, UUID itemId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "userId", userId.toString()));

        CartItem cartItemToRemove = cart.getItems()
                .stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", itemId.toString()));

        // Use the helper method to ensure the relationship is correctly managed
        cart.removeItem(cartItemToRemove);
        Cart updatedCart = cartRepository.save(cart);
        return cartMapper.toDto(updatedCart);
    }

    public CartDto clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "userId", userId.toString()));

        // Use helper method on a copy of the list to avoid ConcurrentModificationException
        new ArrayList<>(cart.getItems()).forEach(cart::removeItem);
        Cart updatedCart = cartRepository.save(cart);
        return cartMapper.toDto(updatedCart);
    }
}