package com.pet.petshop.core.repositories;

import com.pet.petshop.core.entities.Product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @SuppressWarnings("null")
List<Product> findAll();
}
