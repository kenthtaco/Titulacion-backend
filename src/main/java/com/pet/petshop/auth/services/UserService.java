package com.pet.petshop.auth.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pet.petshop.auth.dto.AuthResponse;
import com.pet.petshop.auth.dto.LoginRequest;
import com.pet.petshop.auth.dto.RegisterRequest;
import com.pet.petshop.auth.entity.Role;
import com.pet.petshop.auth.entity.User;
import com.pet.petshop.auth.jwt.JwtService;
import com.pet.petshop.auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    // Registro de ADMIN
    public AuthResponse registerAdmin(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Nombre de usuario ya existe");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(Role.ADMIN)  // Asignación del rol ADMIN
                .build();

        userRepository.save(user);

        String token = jwtService.getToken(user);

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
    }

    // Registro de CUSTOMER
    public AuthResponse registerCustomer(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Nombre de usuario ya existe");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(Role.CUSTOMER)  // Asignación del rol CUSTOMER
                .build();

        userRepository.save(user);

        String token = jwtService.getToken(user);

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
    }

    // Inicio de sesión validando rol específico
    public AuthResponse login(LoginRequest request, Role expectedRole) {
        Optional<User> userOptional = userRepository.findByUsername(request.getUsername());

        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        User user = userOptional.get();

        // Validar contraseña
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Contraseña incorrecta");
        }

        // Validar rol (Asegúrate de que el rol del usuario coincida con el rol esperado)
        if (user.getRole() != expectedRole) {
            throw new IllegalArgumentException("Acceso denegado para este rol");
        }

        // Generar token JWT
        String token = jwtService.getToken(user);

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
    }

    // Inicio de sesión sin rol específico (para manejar login sin restricción de rol)
    public AuthResponse login(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByUsername(request.getUsername());

        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        User user = userOptional.get();

        // Validar contraseña
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Contraseña incorrecta");
        }

        // Generar token JWT
        String token = jwtService.getToken(user);

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
    }
}
