package com.pet.petshop.core.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Datos del cliente
    @NotBlank(message = "El nombre del cliente es obligatorio")
    private String customerName;

    @NotBlank(message = "El apellido del cliente es obligatorio")
    private String customerLastName;

    @NotBlank(message = "La dirección del cliente es obligatoria")
    private String customerAddress;

    @Email(message = "El correo electrónico no es válido")
    private String customerEmail;

    @NotBlank(message = "El número de teléfono es obligatorio")
    private String customerPhone; 

    // Relación con los detalles del pedido
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    private List<OrderDetail> orderDetails = new ArrayList<>();  

    @Builder.Default
    private LocalDate orderDate = LocalDate.now();

    private double totalAmount;

    // 📌 Nuevo campo: Método de pago (tarjeta, paypal, contraentrega)
    @NotBlank(message = "El método de pago es obligatorio")
    private String paymentMethod; // "card", "paypal", "cash_on_delivery"

    // 📌 Nuevo campo: Estado del pedido (PENDIENTE, EN CAMINO, ENTREGADO)
    @Builder.Default
    private String status = "PENDIENTE"; 

    // Método para agregar detalles del pedido
    public void addOrderDetail(OrderDetail detail) {
        orderDetails.add(detail);
        detail.setOrder(this);
    }
}
