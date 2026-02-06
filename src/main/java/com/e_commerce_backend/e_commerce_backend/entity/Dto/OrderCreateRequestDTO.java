package com.e_commerce_backend.e_commerce_backend.entity.Dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderCreateRequestDTO {
    private Long addressId;
    private List<OrderItemRequestDTO> items;
}
