package com.e_commerce_backend.e_commerce_backend.controller;

import org.springframework.web.bind.annotation.RestController;

import com.e_commerce_backend.e_commerce_backend.Utility.helperClass.AuthUtil;
import com.e_commerce_backend.e_commerce_backend.entity.Payment;
import com.e_commerce_backend.e_commerce_backend.services.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final AuthUtil authUtil;

    public PaymentController(PaymentService paymentService, AuthUtil authUtil) {
        this.paymentService = paymentService;
        this.authUtil = authUtil;
    }

    @PostMapping("/create/{orderId}")
    public ResponseEntity<String> createPayment(@PathVariable Long orderId) {

        Long userId = authUtil.loggedInUserId();

        String checkoutUrl = paymentService.createCheckoutSession(orderId, userId);

        return ResponseEntity.ok(checkoutUrl);
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> stripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) throws Exception {

        paymentService.handleStripeWebhook(payload, sigHeader);

        return ResponseEntity.ok("Webhook received");
    }

    @GetMapping("/history")
    public ResponseEntity<List<Payment>> history() {
        Long userId = authUtil.loggedInUserId();
        return ResponseEntity.ok(paymentService.getPaymentHistory(userId));
    }
}

