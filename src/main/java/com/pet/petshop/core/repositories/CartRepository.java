package com.pet.petshop.core.repositories;

import com.pet.petshop.core.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
