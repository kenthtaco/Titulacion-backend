package com.pet.petshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.pet.petshop.auth.jwt.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {

    private final AuthenticationProvider authProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @SuppressWarnings("deprecation")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeRequests(requests -> requests
                        .requestMatchers("/auth/**", "/user/**", "/uploads/**").permitAll() // Permitir acceso público a /uploads/**
                        .requestMatchers("/admin/categories/**").hasAnyRole("ADMIN", "CUSTOMER") // Permitir acceso a ADMIN y CLIENT para categorías
                        .requestMatchers("/admin/products/**", "/admin/orders/**").hasRole("ADMIN")// Cambiado a hasRole
                        .requestMatchers("/dashboard/products/**", "/dashboard/category/**",
                                "/dashboard/contactanos/**", "/dashboard/comentarios/**").hasRole("ADMIN")
                                .requestMatchers("/api/products/**").permitAll() 
                        .requestMatchers("/dashboard-seller/products/**", "/dashboard-seller/order/**",
                                "/dashboard-seller/contactenos/**", "/dashboard-seller/comentarios/**").hasRole("SELLER")
                                .requestMatchers("/dashboard-customer/orders/placeOrder").hasRole("CUSTOMER")
                        .anyRequest().authenticated())
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    private org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        org.springframework.web.cors.CorsConfiguration config = new org.springframework.web.cors.CorsConfiguration();
        config.addAllowedOrigin("http://localhost:4200");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Override
    public void addResourceHandlers(@SuppressWarnings("null") ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/"); // Ruta absoluta de tu carpeta de imágenes
    }
}
