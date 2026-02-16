package com.e_commerce_backend.e_commerce_backend.services;

import com.e_commerce_backend.e_commerce_backend.entity.Payment;

import java.util.List;

public interface PaymentService {
    String createCheckoutSession(Long orderId, Long userId);

    void handleStripeWebhook(String payload, String sigHeader) throws Exception;

    List<Payment> getPaymentHistory(Long userId);
}
