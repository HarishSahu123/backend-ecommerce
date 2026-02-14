package com.e_commerce_backend.e_commerce_backend.controller;

import com.e_commerce_backend.e_commerce_backend.Utility.helperClass.AuthUtil;
import com.e_commerce_backend.e_commerce_backend.services.InvoiceService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InvoiceGenerateController {
    private final  InvoiceService invoiceService;

    private final AuthUtil authUtil;

    public InvoiceGenerateController(InvoiceService invoiceService, AuthUtil authUtil) {
        this.invoiceService = invoiceService;
        this.authUtil = authUtil;
    }


    @GetMapping("/api/invoices/{orderId}")
    public ResponseEntity<byte[]> downloadInvoice(@PathVariable Long orderId) {

        Long userId = authUtil.loggedInUserId(); // From JWT / SecurityContext

        byte[] pdf = invoiceService.generateInvoice(orderId, userId);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=invoice_" + orderId + ".pdf")
                .body(pdf);
    }

}
