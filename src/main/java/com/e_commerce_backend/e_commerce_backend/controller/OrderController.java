package com.e_commerce_backend.e_commerce_backend.controller;

import com.e_commerce_backend.e_commerce_backend.Utility.helperClass.AuthUtil;
import com.e_commerce_backend.e_commerce_backend.entity.Dto.OrderCreateRequestDTO;
import com.e_commerce_backend.e_commerce_backend.entity.dtoResponse.CommonApiResponse;
import com.e_commerce_backend.e_commerce_backend.entity.dtoResponse.OrderResponseDTO;
import com.e_commerce_backend.e_commerce_backend.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<CommonApiResponse<OrderResponseDTO>> placeOrder(@RequestBody OrderCreateRequestDTO dto){
        Long userId = authUtil.loggedInUserId();
        OrderResponseDTO savedOrder= orderService.placeOrder(userId, dto);
       CommonApiResponse<OrderResponseDTO> order=new CommonApiResponse<>(true ,"Request Successfully" ,savedOrder );
        return new ResponseEntity<>(order,  HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<CommonApiResponse<List<OrderResponseDTO>>> getAllOrderByUser( ){
        Long userId = authUtil.loggedInUserId();
        List<OrderResponseDTO> orders = orderService.getAllOrderByUser(userId);

        CommonApiResponse<List<OrderResponseDTO>> response =
                new CommonApiResponse<>(true, "Request Successfully", orders);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
