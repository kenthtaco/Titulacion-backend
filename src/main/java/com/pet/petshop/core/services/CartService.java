package com.pet.petshop.core.services;

import com.pet.petshop.core.entities.Cart;
import com.pet.petshop.core.entities.CartItem;
import com.pet.petshop.core.entities.Product;
import com.pet.petshop.core.repositories.CartRepository;
import com.pet.petshop.core.repositories.CartItemRepository;
import com.pet.petshop.core.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @SuppressWarnings("unused")
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    public Cart addToCart(Long cartId, Long productId, int quantity) {
        // Obtener el carrito
        Cart cart = cartRepository.findById(cartId).orElse(new Cart()); // Si no existe, crear uno nuevo
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Comprobar si el producto ya existe en el carrito
        CartItem existingItem = cart.getItems().stream()
            .filter(item -> item.getProduct().getId().equals(productId))
            .findFirst()
            .orElse(null);

        if (existingItem != null) {
            // Si ya existe, actualizar la cantidad
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            // Si no existe, añadir un nuevo CartItem
            CartItem newItem = CartItem.builder()
                .product(product)
                .quantity(quantity)
                .build();
            cart.getItems().add(newItem);
        }

        // Guardar el carrito actualizado
        return cartRepository.save(cart);
    }

    public Cart getCart(Long cartId) {
        return cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
    }
}
