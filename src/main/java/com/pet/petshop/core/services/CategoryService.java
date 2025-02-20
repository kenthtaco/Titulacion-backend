package com.pet.petshop.core.services;

import com.pet.petshop.core.dto.CategoryResponse;
import com.pet.petshop.core.entities.Category;
import com.pet.petshop.core.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Obtener todas las categorías
    public List<CategoryResponse> getAll() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Obtener una categoría por su ID
    public CategoryResponse getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        return convertToResponse(category);
    }

    // Crear una nueva categoría
    public CategoryResponse save(CategoryResponse categoryResponse) {
        // Verificar si la categoría ya existe
        Category existingCategory = categoryRepository.findByName(categoryResponse.getName());

        if (existingCategory != null) {
            // Si ya existe, devolver la categoría existente
            return convertToResponse(existingCategory);
        }

        // Si no existe, crear la nueva categoría
        Category category = convertToEntity(categoryResponse);
        Category savedCategory = categoryRepository.save(category);
        return convertToResponse(savedCategory);
    }

    // Actualizar una categoría existente
    public CategoryResponse updateCategory(Long id, CategoryResponse categoryResponse) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        
        category.setName(categoryResponse.getName());
        category.setDescription(categoryResponse.getDescription());

        Category updatedCategory = categoryRepository.save(category);
        return convertToResponse(updatedCategory);
    }

    // Eliminar una categoría por su ID
    public String delete(Long id) {
        categoryRepository.deleteById(id);
        return "Categoría con ID " + id + " eliminada correctamente";
    }

    // Convertir la entidad Category a CategoryResponse
    private CategoryResponse convertToResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getDescription());
    }

    // Convertir CategoryResponse a Category para la creación o actualización
    private Category convertToEntity(CategoryResponse categoryResponse) {
        return new Category(categoryResponse.getId(), categoryResponse.getName(), categoryResponse.getDescription());
    }
}
