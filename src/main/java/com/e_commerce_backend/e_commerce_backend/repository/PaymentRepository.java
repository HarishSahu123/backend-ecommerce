package com.e_commerce_backend.e_commerce_backend.repository;

import com.e_commerce_backend.e_commerce_backend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment ,Long> {
}
