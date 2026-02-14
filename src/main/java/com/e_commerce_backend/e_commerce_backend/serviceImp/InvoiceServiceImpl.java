package com.e_commerce_backend.e_commerce_backend.serviceImp;

import com.e_commerce_backend.e_commerce_backend.entity.Invoice;
import com.e_commerce_backend.e_commerce_backend.entity.Orders;
import com.e_commerce_backend.e_commerce_backend.entity.Enum.InvoiceType;
import com.e_commerce_backend.e_commerce_backend.repository.InvoiceRepository;
import com.e_commerce_backend.e_commerce_backend.repository.OrderRepository;
import com.e_commerce_backend.e_commerce_backend.services.InvoicePdfGenerator;
import com.e_commerce_backend.e_commerce_backend.services.InvoiceService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.Year;
import java.util.UUID;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final OrderRepository orderRepository;
    private final InvoiceRepository invoiceRepository;
    private final InvoicePdfGenerator invoicePdfGenerator;

    private final InvoiceHtmlGenerator invoiceHtmlGenerator;

    public InvoiceServiceImpl(OrderRepository orderRepository,
                              InvoiceRepository invoiceRepository,
                              InvoicePdfGenerator invoicePdfGenerator, InvoiceHtmlGenerator invoiceHtmlGenerator) {
        this.orderRepository = orderRepository;
        this.invoiceRepository = invoiceRepository;
        this.invoicePdfGenerator = invoicePdfGenerator;
        this.invoiceHtmlGenerator = invoiceHtmlGenerator;
    }

    @Override
    public byte[] generateInvoice(Long orderId, Long userId) {

        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

        // 🔐 Security check
        if (!order.getUser().getId().equals(userId)) {
            throw new RuntimeException("Access denied");
        }

        // 🧠 Decide invoice type
        if ("PAID".equals(order.getPaymentStatus())) {
            return generateFinalInvoice(order);
        }

        if ("PENDING".equals(order.getPaymentStatus())) {
            return generateProformaInvoice(order);
        }

        throw new RuntimeException("Invoice not available for this order status");
    }

    /* ---------------- PRIVATE METHODS ---------------- */

    private byte[] generateProformaInvoice(Orders order) {

        return invoicePdfGenerator.generateProformaInvoice(order);
    }

    private byte[] generateFinalInvoice(Orders order) {

        Invoice invoice = invoiceRepository.findByOrderId(order.getId())
                .orElseGet(() -> {

                    Invoice newInvoice = Invoice.builder()
                            .order(order)
                            .invoiceType(InvoiceType.FINAL)
                            .invoiceNumber(generateInvoiceNumber())
                            .totalAmount(order.getTotalAmount())
                            .generatedAt(Instant.now())
                            .build();

                    return invoiceRepository.save(newInvoice);
                });

        return invoicePdfGenerator.generateFinalInvoice(order, invoice);
    }

    private String generateInvoiceNumber() {
        return "INV-" + Year.now().getValue() + "-"
                + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }


}
