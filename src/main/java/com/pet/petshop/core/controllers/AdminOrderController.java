package com.pet.petshop.core.controllers;

import com.pet.petshop.core.dto.OrderResponse;
import com.pet.petshop.core.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/orders")
@Tag(name = "Order Management", description = "Endpoints para gestión de pedidos")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    @Operation(summary = "Obtener todos los pedidos")
    @PreAuthorize("hasRole('ADMIN')") // Solo el admin puede ver la lista de pedidos
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        // Obtener todos los pedidos
        List<OrderResponse> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un pedido por su ID")
    @PreAuthorize("hasRole('ADMIN')") // Solo el admin puede ver los detalles del pedido
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        // Obtener el pedido por su ID
        OrderResponse orderResponse = orderService.getOrderById(id);
        return ResponseEntity.ok(orderResponse); // Retornar el pedido encontrado
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un pedido por su ID")
    @PreAuthorize("hasRole('ADMIN')") // Solo el admin puede eliminar pedidos
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        try {
            orderService.deleteOrder(id);  // Llamar al servicio para eliminar el pedido
            return ResponseEntity.noContent().build();  // Retorna 204 No Content si fue exitoso
        } catch (Exception e) {
            return ResponseEntity.status(500).build();  // Retorna un error si ocurre alguna excepción
        }
    }
}
