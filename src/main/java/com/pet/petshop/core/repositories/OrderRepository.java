package com.pet.petshop.core.repositories;

import com.pet.petshop.core.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
