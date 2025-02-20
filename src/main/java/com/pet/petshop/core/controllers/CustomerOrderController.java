package com.pet.petshop.core.controllers;

import com.pet.petshop.core.dto.OrderRequest;
import com.pet.petshop.core.dto.OrderResponse;
import com.pet.petshop.core.services.OrderService;
import com.pet.petshop.core.services.WhatsAppService; // 📌 Nuevo servicio para WhatsApp
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard-customer/orders")
@Tag(name = "Order Management", description = "Endpoints para gestión de pedidos")
public class CustomerOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private WhatsAppService whatsAppService; // 📌 Inyectamos el servicio de WhatsApp

    // Endpoint para que el cliente realice un pedido
    @PostMapping("/placeOrder")
    @Operation(summary = "Realizar un pedido y procesar pago")
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody @Valid OrderRequest orderRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Si hay errores de validación, devolver los detalles del error
            @SuppressWarnings("unused")
            String errorMessages = bindingResult.getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .reduce((message1, message2) -> message1 + ", " + message2)
                .orElse("Error desconocido");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);  // Enviar detalles de error si se desea
        }

        try {
            // Verificar si el número de teléfono está presente antes de procesar el pedido
            if (orderRequest.getCustomerData().getPhone() == null || orderRequest.getCustomerData().getPhone().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(null);  // Retornar un error si el teléfono está vacío
            }

            // Procesar el pedido usando los datos enviados
            OrderResponse orderResponse = orderService.processOrder(orderRequest);

            // Si el método de pago es contraentrega, enviamos un mensaje por WhatsApp
            if ("cash_on_delivery".equalsIgnoreCase(orderRequest.getPaymentMethod())) {  // Actualizado aquí
                String message = "📦 ¡Pedido confirmado!\n" +
                        "🛍️ Total: $" + orderResponse.getTotalAmount() + "\n" +
                        "📍 Dirección: " + orderRequest.getCustomerData().getAddress() + "\n" +
                        "💰 Pago: Contraentrega\n" +
                        "🚚 Tu pedido está en camino. ¡Gracias por comprar con nosotros!";
                whatsAppService.sendMessage(orderRequest.getCustomerData().getPhone(), message);
            }

            // Retornar la respuesta con detalles del pedido
            return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            // Manejar errores en el procesamiento del pedido
            e.printStackTrace();  // Loguear el error completo
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);  // Actualizado a 500 para errores internos
        }
    }
}
