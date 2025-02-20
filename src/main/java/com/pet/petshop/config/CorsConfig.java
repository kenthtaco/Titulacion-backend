package com.pet.petshop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@SuppressWarnings("null") CorsRegistry registry) {
        registry.addMapping("/**")  // Permite CORS para todas las rutas
                .allowedOrigins("http://localhost:4200")  // URL del frontend (puedes agregar más si lo necesitas)
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Métodos permitidos
                .allowedHeaders("*")  // Permite todas las cabeceras
                .exposedHeaders("Authorization")  // Permite la exposición de la cabecera Authorization
                .allowCredentials(true);  // Permite el uso de credenciales si es necesario (si usas cookies de sesión)
    }
}
