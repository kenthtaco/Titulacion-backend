package com.pet.petshop.auth.controller;

import com.pet.petshop.auth.dto.AuthResponse;
import com.pet.petshop.auth.dto.LoginRequest;
import com.pet.petshop.auth.dto.RegisterRequest;
import com.pet.petshop.auth.entity.Role;
import com.pet.petshop.auth.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints para autenticación y gestión de usuarios")
public class AuthController {

    private final UserService userService;

    // Registro de usuario con rol dinámico
    @PostMapping("/register")
    @Operation(summary = "Registrar un nuevo usuario con rol dinámico")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody RegisterRequest request) {
        if (request.getRole() == Role.ADMIN) {
            AuthResponse response = userService.registerAdmin(request);
            return ResponseEntity.ok(response);
        } else if (request.getRole() == Role.CUSTOMER) {
            AuthResponse response = userService.registerCustomer(request);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(null); // Retornar error si el rol no es válido
        }
    }

   // Login de usuario con validación de rol
@PostMapping("/login")
@Operation(summary = "Iniciar sesión validando el rol del usuario")
public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
    try {
        // Se pasa el rol desde la petición
        AuthResponse response = userService.login(request, request.getRole());
        return ResponseEntity.ok(response);  // Retorna el token al frontend
    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(403).body(e.getMessage()); // Manejar errores de autenticación
    }
}

}
