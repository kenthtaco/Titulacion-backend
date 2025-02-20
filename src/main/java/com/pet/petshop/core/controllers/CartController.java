package com.pet.petshop.core.controllers;

import com.pet.petshop.core.entities.Cart;
import com.pet.petshop.core.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/{cartId}/add")
    public Cart addProductToCart(@PathVariable Long cartId, @RequestParam Long productId, @RequestParam int quantity) {
        return cartService.addToCart(cartId, productId, quantity);
    }

    @GetMapping("/{cartId}")
    public Cart getCart(@PathVariable Long cartId) {
        return cartService.getCart(cartId);
    }
}
