package com.e_commerce_backend.e_commerce_backend.entity.dtoResponse;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class OrderResponseDTO {

    private Long orderId;
    private String status;
    private Double totalAmount;
    private Instant createdAt;

    private List<OrderItemResponseDTO> items;
}
