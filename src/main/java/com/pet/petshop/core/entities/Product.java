package com.pet.petshop.core.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private double price;
    
    // Usamos 'productCode' ya que es el que necesitas en el servicio
    private String productCode;  // Código único para el producto

    private int stockQuantity;

    @Lob
    @Column(name = "image", columnDefinition = "bytea")
    private byte[] image;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // Si usas una URL para la imagen, puedes agregar un campo 'imageUrl':
    // private String imageUrl;
}
