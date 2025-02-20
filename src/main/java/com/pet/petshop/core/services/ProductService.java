package com.pet.petshop.core.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pet.petshop.core.dto.ProductResponse;
import com.pet.petshop.core.entities.Category;
import com.pet.petshop.core.entities.Product;
import com.pet.petshop.core.repositories.CategoryRepository;
import com.pet.petshop.core.repositories.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<ProductResponse> getAll() {
        List<Product> products = (List<Product>) productRepository.findAll();
        return products.stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return mapToProductResponse(product);
    }

    @Transactional
    public ProductResponse save(ProductResponse productResponse) {
        Product product = mapToProduct(productResponse);

        // Generar código único para el producto
        String productCode = generateProductCode();
        product.setProductCode(productCode);

        Product savedProduct = productRepository.save(product);
        productRepository.flush();  // Aseguramos que los cambios se apliquen inmediatamente
        return mapToProductResponse(savedProduct);
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductResponse productResponse) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        product.setName(productResponse.getName());
        product.setDescription(productResponse.getDescription());
        product.setPrice(productResponse.getPrice());
        product.setStockQuantity(productResponse.getStockQuantity());

        // Aquí actualizamos el campo de imagen, que ahora es un arreglo de bytes
        if (productResponse.getImage() != null) {
            product.setImage(productResponse.getImage());  // Asignamos la imagen en formato binario
        }

        if (productResponse.getCategoryId() != null) {
            Category category = categoryRepository.findById(productResponse.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            product.setCategory(category);
        }

        Product updatedProduct = productRepository.save(product);
        productRepository.flush();  // Aseguramos que los cambios se apliquen inmediatamente
        return mapToProductResponse(updatedProduct);
    }

    @Transactional
    public String delete(Long id) {
        productRepository.deleteById(id);
        productRepository.flush();  // Aseguramos que el cambio se aplique inmediatamente
        return "Producto con ID " + id + " eliminado correctamente";
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .image(product.getImage()) // Incluimos la imagen en formato binario
                .productCode(product.getProductCode()) // Añadimos el código del producto
                .build();
    }

    private Product mapToProduct(ProductResponse productResponse) {
        Product product = Product.builder()
                .name(productResponse.getName())
                .description(productResponse.getDescription())
                .price(productResponse.getPrice())
                .stockQuantity(productResponse.getStockQuantity())
                .image(productResponse.getImage()) // Mapeamos la imagen en formato binario
                .build();

        if (productResponse.getCategoryId() != null) {
            Category category = categoryRepository.findById(productResponse.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            product.setCategory(category);
        }

        return product;
    }

    // Método para generar un código único para el producto
    private String generateProductCode() {
        // Utilizamos UUID para generar un código único basado en tiempo y valor aleatorio
        String uniqueCode = "PROD-" + UUID.randomUUID().toString().substring(0, 8);
        return uniqueCode;
    }
}
