package com.e_commerce_backend.e_commerce_backend.services;

import com.e_commerce_backend.e_commerce_backend.entity.Invoice;
import com.e_commerce_backend.e_commerce_backend.entity.Orders;

public interface InvoicePdfGenerator {

    byte[] generateProformaInvoice(Orders order);

    byte[] generateFinalInvoice(Orders order, Invoice invoice);
}
