package com.pet.petshop.auth.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    CUSTOMER,
    ADMIN;

    @JsonCreator
    public static Role fromValue(String value) {
        // Convierte el valor recibido a mayúsculas para que coincida con los valores del enum
        try {
            return Role.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Rol no válido: " + value);
        }
    }

    @JsonValue
    public String toValue() {
        return name().toLowerCase(); // Opcional: para enviar siempre el rol en minúsculas al frontend
    }
}
