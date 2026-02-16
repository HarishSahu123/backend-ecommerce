package com.e_commerce_backend.e_commerce_backend.repository;

import com.e_commerce_backend.e_commerce_backend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment ,Long> {
    Optional<Payment> findByStripeSessionId(String sessionId);

    Optional<Payment> findByStripePaymentIntentId(String paymentIntentId);

    List<Payment> findByOrderUserId(Long userId);
}
