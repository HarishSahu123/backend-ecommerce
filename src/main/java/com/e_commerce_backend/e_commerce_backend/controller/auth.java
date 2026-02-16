package com.e_commerce_backend.e_commerce_backend.controller;

import com.e_commerce_backend.e_commerce_backend.entity.Dto.UserDTO;
import com.e_commerce_backend.e_commerce_backend.entity.Request.LoginRequestdto;
import com.e_commerce_backend.e_commerce_backend.entity.dtoResponse.CommonApiResponse;
import com.e_commerce_backend.e_commerce_backend.entity.dtoResponse.LoginResponsedto;
import com.e_commerce_backend.e_commerce_backend.entity.dtoResponse.UserResponseDTO;
import com.e_commerce_backend.e_commerce_backend.serviceImp.AuthService;
import com.e_commerce_backend.e_commerce_backend.serviceImp.UserRoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/public")
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
    public ResponseEntity<UserResponseDTO> signup(@RequestBody UserDTO user){
        return ResponseEntity.ok(userRoleService.createUser(user));
    }



    @PostMapping("/createUser")
    public ResponseEntity<CommonApiResponse> createUser(@RequestBody UserDTO user){
        UserResponseDTO user1 = userRoleService.createUser(user);
        CommonApiResponse commonApiResponse=new CommonApiResponse<>(true ,"User create successfully" ,user1);
        return new ResponseEntity<>(commonApiResponse ,HttpStatus.OK
        );
    }

}
