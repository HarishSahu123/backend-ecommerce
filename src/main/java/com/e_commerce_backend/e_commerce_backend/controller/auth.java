package com.e_commerce_backend.e_commerce_backend.controller;

import com.e_commerce_backend.e_commerce_backend.entity.Request.LoginRequestdto;
import com.e_commerce_backend.e_commerce_backend.entity.UserEntity;
import com.e_commerce_backend.e_commerce_backend.entity.dtoResponse.LoginResponsedto;
import com.e_commerce_backend.e_commerce_backend.serviceImp.AuthService;
import com.e_commerce_backend.e_commerce_backend.serviceImp.UserRoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.PublicKey;

@RestController
@RequestMapping("/api/v1")
public class auth {
    @Autowired
    private final AuthService authService;

    private final UserRoleServiceImpl userRoleService;
    public auth(AuthService authService, UserRoleServiceImpl userRoleService) {
        this.authService = authService;
        this.userRoleService = userRoleService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponsedto> Login(@RequestBody LoginRequestdto loginRequestdto){
        return ResponseEntity.ok(authService.login(loginRequestdto));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserEntity> signup(@RequestBody UserEntity user){
        return ResponseEntity.ok(userRoleService.createUser(user));
    }



    @PostMapping("/createUser")
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity user){
        UserEntity user1 = userRoleService.createUser(user);
        ResponseEntity<UserEntity> userEntityResponseEntity = new ResponseEntity<>(user1, HttpStatus.CREATED);
        return userEntityResponseEntity;
    }








}
