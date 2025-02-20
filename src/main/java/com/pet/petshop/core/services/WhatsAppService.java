package com.pet.petshop.core.services;

import org.springframework.stereotype.Service;

@Service
public class WhatsAppService {
    public void sendMessage(String phoneNumber, String message) {
        // Implementación para enviar mensaje por WhatsApp
        System.out.println("Enviando mensaje a " + phoneNumber + ": " + message);
    }
}
