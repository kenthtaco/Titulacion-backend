package com.pet.petshop.auth.controller;

import com.pet.petshop.auth.dto.AuthResponse;
import com.pet.petshop.auth.dto.RegisterRequest;
import com.pet.petshop.auth.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "Endpoints para registro de usuarios")
public class UserController {

    private final UserService userService;

    // Registrar ADMIN
    @PostMapping(value = "/admin")
    @Operation(summary = "Registrar un nuevo administrador")
    public ResponseEntity<AuthResponse> registerAdmin(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.registerAdmin(request));
    }

    // Registrar SELLER (vendedor)
    @PostMapping(value = "/seller")
    @Operation(summary = "Registrar un nuevo vendedor")
    public ResponseEntity<AuthResponse> registerSeller(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.registerCustomer(request));  // Corregido aquí
    }

    // Registrar CUSTOMER (cliente)
    @PostMapping(value = "/customer")
    @Operation(summary = "Registrar un nuevo cliente")
    public ResponseEntity<AuthResponse> registerCustomer(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.registerCustomer(request));
    }
}
