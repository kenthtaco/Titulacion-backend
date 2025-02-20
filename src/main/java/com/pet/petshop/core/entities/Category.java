package com.pet.petshop.core.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 255)  // Limite de 255 caracteres para el nombre de la categoría
    private String name;

    @Size(max = 255)  // Limite de 255 caracteres para la descripción
    private String description;
}
