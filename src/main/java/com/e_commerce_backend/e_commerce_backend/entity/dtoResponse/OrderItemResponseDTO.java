package com.e_commerce_backend.e_commerce_backend.entity.dtoResponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemResponseDTO {

    private Long productId;
    private String productName;
    private Integer quantity;
    private Double price;
    private Double totalPrice;
}
