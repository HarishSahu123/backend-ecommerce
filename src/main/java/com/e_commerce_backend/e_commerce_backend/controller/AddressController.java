package com.e_commerce_backend.e_commerce_backend.controller;

import com.e_commerce_backend.e_commerce_backend.Utility.helperClass.AuthUtil;
import com.e_commerce_backend.e_commerce_backend.entity.Dto.AddressDTO;
import com.e_commerce_backend.e_commerce_backend.entity.dtoResponse.AddressResponse;
import com.e_commerce_backend.e_commerce_backend.serviceImp.AddressServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AddressController {

    private final AddressServiceImpl addressService;
    private final AuthUtil authUtil;

    public AddressController(AddressServiceImpl addressService, AuthUtil authUtil) {
        this.addressService = addressService;
        this.authUtil = authUtil;
    }

    @PostMapping("/save-address")
    public ResponseEntity<AddressDTO> addUserAddress(@RequestBody AddressDTO addressDTO){
        Long userId = authUtil.loggedInUserId();
        AddressDTO userAddress = addressService.addUserAddress(addressDTO, userId);
        return new ResponseEntity<>(userAddress , HttpStatus.FOUND);
    }

//   http://localhost:8081/api/v1/user/{user_id}/get-address
    @GetMapping("/user/{user_id}/get-address")
    public ResponseEntity<AddressResponse> getUserAddress(@PathVariable Long user_id ){
        AddressResponse userAddress = addressService.getUserAddress(user_id);
        return new ResponseEntity<>(userAddress,  HttpStatus.FOUND);
    }


}
