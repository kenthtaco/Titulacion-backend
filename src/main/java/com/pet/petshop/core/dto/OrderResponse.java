package com.pet.petshop.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private String customerName;  // Nombre del cliente
    private String customerLastName;  // Apellido del cliente
    private String customerAddress;  // Dirección del cliente
    private String customerEmail;  // Correo electrónico del cliente
    private String customerPhone;  // Número de teléfono del cliente
    private LocalDate orderDate;  // Fecha del pedido
    private double totalAmount;  // Monto total del pedido
    private List<OrderDetailResponse> orderDetails;  // Detalles del pedido

    // Los métodos getter y setter se generarán automáticamente por Lombok
}
