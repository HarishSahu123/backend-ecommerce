package com.e_commerce_backend.e_commerce_backend.controller;

import com.e_commerce_backend.e_commerce_backend.Utility.helperClass.AuthUtil;
import com.e_commerce_backend.e_commerce_backend.entity.Dto.OrderCreateRequestDTO;
import com.e_commerce_backend.e_commerce_backend.entity.dtoResponse.AddressResponse;
import com.e_commerce_backend.e_commerce_backend.entity.dtoResponse.CommonApiResponse;
import com.e_commerce_backend.e_commerce_backend.entity.dtoResponse.OrderResponseDTO;
import com.e_commerce_backend.e_commerce_backend.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    @Autowired
    private final OrderService orderService;
    
    
    private final AuthUtil authUtil;

    public OrderController(OrderService orderService, AuthUtil authUtil) {
        this.orderService = orderService;
        this.authUtil = authUtil;
    }

    @PostMapping
    public ResponseEntity<CommonApiResponse<OrderResponseDTO>> createOrder(@RequestBody OrderCreateRequestDTO dto){
        Long userId = authUtil.loggedInUserId();
        OrderResponseDTO savedOrder= orderService.placeOrder(userId, dto);
       CommonApiResponse<OrderResponseDTO> order=new CommonApiResponse<>(true ,"Request Successfully" ,savedOrder );
        return new ResponseEntity<>(order,  HttpStatus.CREATED);
    }
}
