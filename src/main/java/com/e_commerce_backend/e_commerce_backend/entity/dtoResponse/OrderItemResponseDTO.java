package com.e_commerce_backend.e_commerce_backend.entity.dtoResponse;

import com.e_commerce_backend.e_commerce_backend.entity.Orders;
import com.e_commerce_backend.e_commerce_backend.entity.Product;
import jakarta.persistence.ManyToOne;
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
