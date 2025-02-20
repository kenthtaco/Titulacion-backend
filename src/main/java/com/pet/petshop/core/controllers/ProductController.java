package com.pet.petshop.core.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.pet.petshop.core.dto.ProductResponse;
import com.pet.petshop.core.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/admin/products")
@Tag(name = "Product Management", description = "Endpoints para gestión de productos")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    @Operation(summary = "Obtener todos los productos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = productService.getAll();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un producto por su ID")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse product = productService.getById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PostMapping(consumes = { "multipart/form-data" })
    @Operation(summary = "Crear un nuevo producto con imagen")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> createProduct(
            @RequestPart("product") ProductResponse productResponse,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                byte[] imageBytes = imageFile.getBytes(); // Guardamos la imagen como un arreglo de bytes
                productResponse.setImage(imageBytes); // Asignamos los bytes de la imagen
            } catch (IOException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        ProductResponse createdProduct = productService.save(productResponse);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = { "multipart/form-data" })
    @Operation(summary = "Actualizar un producto existente con imagen")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @RequestPart("product") ProductResponse productResponse,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                byte[] imageBytes = imageFile.getBytes(); // Guardamos la imagen como un arreglo de bytes
                productResponse.setImage(imageBytes); // Asignamos los bytes de la imagen
            } catch (IOException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        ProductResponse updatedProduct = productService.updateProduct(id, productResponse);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

  @DeleteMapping("/{id}")
@Operation(summary = "Eliminar un producto por su ID")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable Long id) {
    boolean isDeleted = productService.delete(id) != null;
    if (isDeleted) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Producto eliminado exitosamente");
        return new ResponseEntity<>(response, HttpStatus.OK); // Respuesta 200 OK con mensaje en formato JSON
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Respuesta 404 si no se encuentra el producto
    }
}

}
