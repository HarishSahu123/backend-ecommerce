package com.e_commerce_backend.e_commerce_backend.serviceImp;

import com.e_commerce_backend.e_commerce_backend.entity.Enum.OrderStatus;
import com.e_commerce_backend.e_commerce_backend.entity.Orders;
import com.e_commerce_backend.e_commerce_backend.entity.Payment;
import com.e_commerce_backend.e_commerce_backend.repository.OrderRepository;
import com.e_commerce_backend.e_commerce_backend.repository.PaymentRepository;
import com.e_commerce_backend.e_commerce_backend.services.InvoiceService;
import com.e_commerce_backend.e_commerce_backend.services.PaymentService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

//    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

//    @Value("${stripe.webhook.secret}")
    private String stripeWebhookSecret;

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final InvoiceService invoiceService;

    public PaymentServiceImpl(OrderRepository orderRepository,
                              PaymentRepository paymentRepository,
                              InvoiceService invoiceService) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.invoiceService = invoiceService;
    }

    // ============================================================
    // CREATE CHECKOUT SESSION
    // ============================================================
    @Override
    public String createCheckoutSession(Long orderId, Long userId) {

        Stripe.apiKey = stripeSecretKey;

        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getId().equals(userId)) {
            throw new RuntimeException("Access denied");
        }

        try {

            SessionCreateParams params =
                    SessionCreateParams.builder()
                            .setMode(SessionCreateParams.Mode.PAYMENT)
                            .setSuccessUrl("http://localhost:3000/payment-success")
                            .setCancelUrl("http://localhost:3000/payment-failed")
                            .putMetadata("orderId", order.getId().toString())
                            .addLineItem(
                                    SessionCreateParams.LineItem.builder()
                                            .setQuantity(1L)
                                            .setPriceData(
                                                    SessionCreateParams.LineItem.PriceData.builder()
                                                            .setCurrency("inr")
                                                            .setUnitAmount((long) (order.getTotalAmount() * 100))
                                                            .setProductData(
                                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                            .setName("Order #" + order.getId())
                                                                            .build())
                                                            .build())
                                            .build())
                            .build();

            Session session = Session.create(params);

            Payment payment = Payment.builder()
                    .stripeSessionId(session.getId())
                    .order(order)
                    .amount(order.getTotalAmount())
                    .currency("INR")
                    .paymentStatus("PENDING")
                    .createdAt(Instant.now())
                    .build();

            paymentRepository.save(payment);

            return session.getUrl();

        } catch (StripeException e) {
            throw new RuntimeException("Stripe session creation failed", e);
        }
    }

    // ============================================================
    // STRIPE WEBHOOK HANDLER
    // ============================================================
    @Override
    public void handleStripeWebhook(String payload, String sigHeader) throws Exception {

        Stripe.apiKey = stripeSecretKey;

        Event event;

        try {
            event = Webhook.constructEvent(
                    payload,
                    sigHeader,
                    stripeWebhookSecret
            );
        } catch (Exception e) {
            System.out.println("❌ Invalid webhook signature");
            throw e;
        }

        System.out.println("🔥 Stripe Webhook Received: " + event.getType());

        switch (event.getType()) {

            // =========================================================
            // ✅ CHECKOUT SESSION COMPLETED (MAIN SUCCESS EVENT)
            // =========================================================
            case "checkout.session.completed":

                Session session = Session.GSON.fromJson(
                        event.getData().getObject().toJson(),
                        Session.class
                );

                if (session == null) {
                    System.out.println("❌ Session deserialization failed");
                    return;
                }

                String sessionId = session.getId();
                String paymentIntentId = session.getPaymentIntent();

                System.out.println("Session ID: " + sessionId);
                System.out.println("PaymentIntent ID: " + paymentIntentId);

                Payment payment = paymentRepository
                        .findByStripeSessionId(sessionId)
                        .orElse(null);

                if (payment == null) {
                    System.out.println("❌ Payment not found for session");
                    return;
                }

                // 🔐 Idempotency check (avoid duplicate update)
                if ("SUCCESS".equals(payment.getPaymentStatus())) {
                    System.out.println("⚠️ Payment already processed");
                    return;
                }

                updatePaymentSuccess(payment, paymentIntentId);
                break;


            // =========================================================
            // ❌ PAYMENT FAILED
            // =========================================================
            case "checkout.session.expired":

                Session expiredSession = Session.GSON.fromJson(
                        event.getData().getObject().toJson(),
                        Session.class
                );

                if (expiredSession == null) return;

                Payment expiredPayment = paymentRepository
                        .findByStripeSessionId(expiredSession.getId())
                        .orElse(null);

                if (expiredPayment != null) {
                    expiredPayment.setPaymentStatus("FAILED");
                    paymentRepository.save(expiredPayment);
                    System.out.println("❌ Payment marked as FAILED");
                }

                break;

            default:
                System.out.println("Unhandled event type: " + event.getType());
        }
    }


    // ============================================================
    // UPDATE PAYMENT + ORDER + GENERATE INVOICE
    // ============================================================
    private void updatePaymentSuccess(Payment payment, String paymentIntentId) {

        payment.setPaymentStatus("SUCCESS");
        payment.setStripePaymentIntentId(paymentIntentId);

        Orders order = payment.getOrder();
        order.setPaymentStatus("PAID");
        order.setOrderStatus(OrderStatus.CONFIRMED);

        paymentRepository.save(payment);
        orderRepository.save(order);

        invoiceService.generateInvoice(order.getId(), order.getUser().getId());

        System.out.println("✅ Payment updated successfully!");
    }


    // ============================================================
    // PAYMENT HISTORY
    // ============================================================
    @Override
    public List<Payment> getPaymentHistory(Long userId) {
        return paymentRepository.findByOrderUserId(userId);
    }
}
