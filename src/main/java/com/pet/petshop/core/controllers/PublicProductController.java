package com.pet.petshop.core.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pet.petshop.core.dto.ProductResponse;
import com.pet.petshop.core.services.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Public Product Management", description = "Endpoints públicos para visualizar productos")
public class PublicProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    @Operation(summary = "Obtener todos los productos disponibles (público)")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        // Llamar al servicio para obtener todos los productos
        List<ProductResponse> products = productService.getAll();
        return new ResponseEntity<>(products, HttpStatus.OK); // Devolver productos con estado 200 OK
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un producto por su ID (público)")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        // Llamar al servicio para obtener un producto por su ID
        ProductResponse product = productService.getById(id);
        return new ResponseEntity<>(product, HttpStatus.OK); // Devolver producto con estado 200 OK
    }
}
