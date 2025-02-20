package com.pet.petshop.core.services;

import com.pet.petshop.core.dto.CustomerData;
import com.pet.petshop.core.dto.OrderRequest;
import com.pet.petshop.core.dto.OrderResponse;
import com.pet.petshop.core.dto.OrderDetailResponse;
import com.pet.petshop.core.entities.Order;
import com.pet.petshop.core.entities.OrderDetail;
import com.pet.petshop.core.entities.Product;
import com.pet.petshop.core.repositories.OrderRepository;
import com.pet.petshop.core.repositories.OrderDetailRepository;
import com.pet.petshop.core.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WhatsAppService whatsAppService;  // Inyectamos el servicio de WhatsApp

    // Método para procesar el pedido
    public OrderResponse processOrder(OrderRequest orderRequest) throws Exception {
        // Obtener los datos del cliente desde el DTO CustomerData
        CustomerData customerData = orderRequest.getCustomerData();

        // Verificar si el número de teléfono está presente
        if (customerData.getPhone() == null || customerData.getPhone().isEmpty()) {
            throw new RuntimeException("El número de teléfono del cliente es obligatorio.");
        }

        Order order = Order.builder()
        .customerName(customerData.getFirstName())
        .customerLastName(customerData.getLastName())
        .customerAddress(customerData.getAddress())
        .customerEmail(customerData.getEmail())
        .customerPhone(customerData.getPhone())
        .orderDate(java.time.LocalDate.now())
        .totalAmount(orderRequest.getTotalAmount())
        .paymentMethod(orderRequest.getPaymentMethod())  // Asignar el método de pago
        .build();


        try {
            // Guardar la orden en la base de datos
            orderRepository.save(order);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar la orden: " + e.getMessage(), e);
        }

        // Crear y guardar los detalles de la orden sin la imagen del producto
        List<OrderDetail> orderDetails = orderRequest.getOrderDetails().stream().map(itemRequest -> {
            try {
                // Solo se recuperan los datos necesarios del producto (sin la imagen)
                Product product = productRepository.findById(itemRequest.getProductId())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + itemRequest.getProductId()));

                // Crear el detalle de la orden con los datos del producto (sin la imagen)
                OrderDetail orderDetail = OrderDetail.builder()
                        .order(order)
                        .product(product)  // Solo se usa el producto sin la imagen
                        .quantity(itemRequest.getQuantity())
                        .productCode(product.getProductCode())
                        .price(itemRequest.getPrice())
                        .build();

                // Guardar el detalle de la orden
                return orderDetailRepository.save(orderDetail);

            } catch (Exception e) {
                throw new RuntimeException("Error al procesar los detalles del pedido: " + e.getMessage(), e);
            }
        }).collect(Collectors.toList());

        // Asociar los detalles a la orden
        order.setOrderDetails(orderDetails);

        // Si el pago es contra entrega, enviamos un mensaje por WhatsApp
        if ("cash_on_delivery".equals(orderRequest.getPaymentMethod())) {
            String message = "Hola " + order.getCustomerName() + ", tu pedido está listo para ser entregado. El pago será contra entrega. ¡Gracias por tu compra!";
            try {
                whatsAppService.sendMessage(order.getCustomerPhone(), message);  // Asegúrate de que el teléfono esté en los datos del cliente
            } catch (Exception e) {
                throw new RuntimeException("Error al enviar mensaje por WhatsApp: " + e.getMessage(), e);
            }
        }

        // Crear la respuesta con los detalles del pedido
        return buildOrderResponse(order);
    }

    // Método para construir la respuesta del pedido
    private OrderResponse buildOrderResponse(Order order) {
        List<OrderDetailResponse> orderDetailResponses = order.getOrderDetails().stream()
            .map((OrderDetail detail) -> {
                return OrderDetailResponse.builder()
                    .productId(detail.getProduct().getId())
                    .productName(detail.getProduct().getName())
                    .productCode(detail.getProductCode()) 
                    .quantity(detail.getQuantity())
                    .price(detail.getPrice())
                    .build();
            })
            .collect(Collectors.toList());

        return OrderResponse.builder()
                .id(order.getId())
                .customerName(order.getCustomerName())
                .customerLastName(order.getCustomerLastName())
                .customerAddress(order.getCustomerAddress())
                .customerEmail(order.getCustomerEmail())
                .customerPhone(order.getCustomerPhone()) // Asegúrate de incluir el número de teléfono
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .orderDetails(orderDetailResponses)
                .build();
    }

    // Método para obtener todos los pedidos para el administrador
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(this::buildOrderResponse).collect(Collectors.toList());
    }

    // Método para obtener un pedido específico por su ID (para el administrador)
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        return buildOrderResponse(order);
    }

    // Método para eliminar un pedido por su ID
    public void deleteOrder(Long id) {
        // Verificar si el pedido existe
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        // Eliminar los detalles del pedido
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrder(order);
        orderDetailRepository.deleteAll(orderDetails);

        // Eliminar la orden
        orderRepository.delete(order);
    }
}
