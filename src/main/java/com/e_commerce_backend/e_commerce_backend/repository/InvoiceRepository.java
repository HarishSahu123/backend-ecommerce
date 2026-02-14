package com.e_commerce_backend.e_commerce_backend.repository;

import com.e_commerce_backend.e_commerce_backend.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findByOrderId(Long orderId);

    boolean existsByInvoiceNumber(String invoiceNumber);
}