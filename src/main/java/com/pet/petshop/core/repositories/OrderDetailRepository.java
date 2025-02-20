package com.pet.petshop.core.repositories;

import com.pet.petshop.core.entities.Order;
import com.pet.petshop.core.entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    
    // Método para encontrar los detalles de un pedido en base a la orden
    List<OrderDetail> findByOrder(Order order);
}
