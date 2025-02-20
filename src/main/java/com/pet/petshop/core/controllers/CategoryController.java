package com.pet.petshop.core.controllers;

import com.pet.petshop.core.dto.CategoryResponse;
import com.pet.petshop.core.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/categories")
@Tag(name = "Category Management", description = "Endpoints para gestión de categorías de productos")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // Obtener todas las categorías
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")  // Cambiado a hasRole
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> categories = categoryService.getAll();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    // Obtener una categoría por su ID
    @GetMapping("/{id}")
    @Operation(summary = "Obtener una categoría por su ID")
    @PreAuthorize("hasRole('ADMIN')")  // Cambiado a hasRole
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
        CategoryResponse category = categoryService.getById(id);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    // Crear una nueva categoría
    @PostMapping
    @Operation(summary = "Crear una nueva categoría")
    @PreAuthorize("hasRole('ADMIN')")  // Cambiado a hasRole
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryResponse categoryResponse) {
        CategoryResponse createdCategory = categoryService.save(categoryResponse);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    // Actualizar una categoría existente
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una categoría existente")
    @PreAuthorize("hasRole('ADMIN')")  // Cambiado a hasRole
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long id, @RequestBody CategoryResponse categoryResponse) {
        CategoryResponse updatedCategory = categoryService.updateCategory(id, categoryResponse);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    // Eliminar una categoría por su ID
    @DeleteMapping("/{id}")
@Operation(summary = "Eliminar una categoría por su ID")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<Map<String, String>> deleteCategory(@PathVariable Long id) {
    boolean isDeleted = categoryService.delete(id) != null;
    if (isDeleted) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Categoría eliminada exitosamente");
        return new ResponseEntity<>(response, HttpStatus.OK); // Respuesta 200 OK con mensaje en formato JSON
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Respuesta 404 si no se encuentra la categoría
    }
}

}
