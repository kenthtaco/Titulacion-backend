package com.pet.petshop.core.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "order_details")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con la orden
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Relación con el producto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Cantidad del producto (mínimo 1)
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    @Column(nullable = false)
    private int quantity;

    // Precio unitario del producto (no negativo)
    @Min(value = 0, message = "El precio no puede ser negativo")
    @Column(nullable = false)
    private double price;

    @Column(length = 50)
    private String productCode;

    // Métodos auxiliares

    @Transient
    public Long getProductId() {
        return product != null ? product.getId() : null;
    }

    @Transient
    public String getProductName() {
        return product != null ? product.getName() : "Producto desconocido";
    }

    @Transient
    public double getTotalPrice() {
        return price * quantity;
    }

}
