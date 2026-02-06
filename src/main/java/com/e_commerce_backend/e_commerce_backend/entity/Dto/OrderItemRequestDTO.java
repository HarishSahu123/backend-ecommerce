package com.e_commerce_backend.e_commerce_backend.entity.Dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class OrderItemRequestDTO {
    private Long productId;
    private Integer quantity;
}
