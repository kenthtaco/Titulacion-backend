package com.pet.petshop.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    
    private Long id;
    private String name;
    private String description;
    private double price;
    private int stockQuantity;
    private Long categoryId; 
    private byte[] image;
    private String productCode;  
    
     // Getters y Setters para todos los campos
     public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }// Cambiado de imageUrl a image en formato binario
}
