package com.pet.petshop.auth.dto;

import com.pet.petshop.auth.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    private String username;
    private String password;
    private Role role; // Rol esperado (ADMIN o CUSTOMER)
}

