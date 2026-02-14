package com.e_commerce_backend.e_commerce_backend.services;

public interface InvoiceService {
    byte[] generateInvoice(Long orderId, Long userId);}
