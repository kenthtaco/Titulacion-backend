package com.pet.petshop.core.repositories;

import com.pet.petshop.core.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
