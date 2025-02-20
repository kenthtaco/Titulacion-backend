package com.pet.petshop.core.dto;

import java.util.List;

public class OrderRequest {
    
    private CustomerData customerData;
    private List<OrderDetailRequest> orderDetails;
    private double totalAmount;
    private String paymentMethod; // 📌 Agregado para definir el tipo de pago

    // Getters y setters
    public CustomerData getCustomerData() {
        return customerData;
    }

    public void setCustomerData(CustomerData customerData) {
        this.customerData = customerData;
    }

    public List<OrderDetailRequest> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetailRequest> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
