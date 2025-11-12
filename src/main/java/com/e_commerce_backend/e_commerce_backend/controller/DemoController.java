package com.e_commerce_backend.e_commerce_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class DemoController {
    @GetMapping("/user")
    public String createUser(){
        return "Created Successfully";
    }

    @GetMapping("/public")
    public String general(){
        return "General Method";
    }

    @GetMapping("/admin")
    public String adminMethod(){
        return "welcome admin";
    }
}
