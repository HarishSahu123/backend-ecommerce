package com.e_commerce_backend.e_commerce_backend.services;

import com.e_commerce_backend.e_commerce_backend.entity.Dto.OrderCreateRequestDTO;
import com.e_commerce_backend.e_commerce_backend.entity.dtoResponse.OrderResponseDTO;

public interface OrderService {

     OrderResponseDTO placeOrder(Long userId, OrderCreateRequestDTO request);

    }
