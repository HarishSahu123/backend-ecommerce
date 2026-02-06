package com.e_commerce_backend.e_commerce_backend.serviceImp;

import com.e_commerce_backend.e_commerce_backend.Exception.APIException;
import com.e_commerce_backend.e_commerce_backend.Exception.ResourceNotFoundException;
import com.e_commerce_backend.e_commerce_backend.entity.*;
import com.e_commerce_backend.e_commerce_backend.entity.Dto.OrderCreateRequestDTO;
import com.e_commerce_backend.e_commerce_backend.entity.Dto.OrderItemRequestDTO;
import com.e_commerce_backend.e_commerce_backend.entity.Enum.OrderStatus;
import com.e_commerce_backend.e_commerce_backend.entity.dtoResponse.OrderItemResponseDTO;
import com.e_commerce_backend.e_commerce_backend.entity.dtoResponse.OrderResponseDTO;
import com.e_commerce_backend.e_commerce_backend.repository.AddressRepository;
import com.e_commerce_backend.e_commerce_backend.repository.OrderRepository;
import com.e_commerce_backend.e_commerce_backend.repository.ProductRepository;
import com.e_commerce_backend.e_commerce_backend.repository.UserRepository;
import com.e_commerce_backend.e_commerce_backend.services.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

        private final UserRepository userRepository;
        private final AddressRepository addressRepository;
        private final ProductRepository productRepository;
        private final OrderRepository orderRepository;

    public OrderServiceImpl(UserRepository userRepository, AddressRepository addressRepository, ProductRepository productRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
        @Override
        public OrderResponseDTO placeOrder(Long userId, OrderCreateRequestDTO request) {

            // 1️⃣ Validate user
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User" ,"userId" ,+ userId));

            // 2️⃣ Validate address
            Address address = addressRepository.findById(request.getAddressId())
                    .orElseThrow(() -> new ResourceNotFoundException("Address", "AddressID" ,"Address"));

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new APIException("Order must contain at least one item");
        }


        // 3️⃣ Create order
        Orders orders=new Orders();
        orders.setUser(user);
        orders.setOrderNumber(UUID.randomUUID().toString());
        orders.setPaymentStatus("PENDING");
        orders.setAddress(address);
        orders.setOrderStatus(OrderStatus.PENDING);
        orders.setCreatedAt(Instant.now());

            double totalAmount = 0.0;
            List<OrderItem> orderItems = new ArrayList<>();

            // 4️⃣ Process order items
            for (OrderItemRequestDTO item : request.getItems()) {

                Product product = productRepository.findById(item.getProductId())
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found" ,"", ""));

                if (product.getQuality() < item.getQuantity()) {
                    throw new APIException("Insufficient stock for product: " + product.getProductName());
                }

                product.setQuality(product.getQuality() - item.getQuantity());

                double itemTotal = product.getPrice() * item.getQuantity();
                totalAmount += itemTotal;


                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(orders);
                orderItem.setProduct(product);
                orderItem.setQuantity(item.getQuantity());
                orderItem.setPrice(product.getPrice());
                orderItem.setTotalPrice(product.getPrice() * item.getQuantity());

                orderItems.add(orderItem);
            }

            orders.setOrderItems(orderItems);
            orders.setTotalAmount(totalAmount);

            // 5️⃣ Save order
            Orders savedOrder = orderRepository.save(orders);

            // 6️⃣ Map to response
            return mapToResponse(savedOrder);
        }

        private OrderResponseDTO mapToResponse(Orders order) {

            List<OrderItemResponseDTO> items = order.getOrderItems().stream()
                    .map(item -> OrderItemResponseDTO.builder()
                            .productId(item.getProduct().getProduct_id())
                            .productName(item.getProduct().getProductName())
                            .quantity(item.getQuantity())
                            .price(item.getPrice())
                            .totalPrice(item.getPrice() * item.getQuantity())
                            .build())
                    .toList();

            return OrderResponseDTO.builder()
                    .orderId(order.getId())
                    .status(order.getOrderStatus().name())
                    .totalAmount(order.getTotalAmount())
                    .createdAt(order.getCreatedAt())
                    .items(items)
                    .build();
        }
}
