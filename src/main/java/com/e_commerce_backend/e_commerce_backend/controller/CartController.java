package com.e_commerce_backend.e_commerce_backend.controller;

import com.e_commerce_backend.e_commerce_backend.Exception.APIException;
import com.e_commerce_backend.e_commerce_backend.Exception.ResourceNotFoundException;
import com.e_commerce_backend.e_commerce_backend.Utility.helperClass.AuthUtil;
import com.e_commerce_backend.e_commerce_backend.entity.Carts;
import com.e_commerce_backend.e_commerce_backend.entity.Dto.CartDTO;
import com.e_commerce_backend.e_commerce_backend.entity.Dto.CategoryDTO;
import com.e_commerce_backend.e_commerce_backend.entity.UserEntity;
import com.e_commerce_backend.e_commerce_backend.repository.CartRepository;
import com.e_commerce_backend.e_commerce_backend.serviceImp.CartServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class CartController {

    private final CartServiceImpl cartService;
    private final AuthUtil authUtil;

    private final CartRepository cartRepository;


    public CartController(CartServiceImpl cartService, AuthUtil authUtil, CartRepository cartRepository) {
        this.cartService = cartService;
        this.authUtil = authUtil;
        this.cartRepository = cartRepository;
    }


    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId ,
                                                    @PathVariable Integer quantity){
        CartDTO cartDTO=cartService.addProductTOcart(productId,quantity);
        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
    }

    @GetMapping("/carts/list")
    public ResponseEntity<List<CartDTO>> getAllCart(){
        List<CartDTO> cartDTOS=cartService.getAllCarts();
        return new ResponseEntity<List<CartDTO>>(cartDTOS,HttpStatus.FOUND);
    }

    @GetMapping("/carts/user/cart")
    public ResponseEntity<CartDTO> getCartByUser(){
        String email = authUtil.loggedInEmail();

        Optional<Carts> cartOpt = cartRepository.findCartByUsername(email);
        if(cartOpt.isEmpty()){
            throw new ResourceNotFoundException("Cart", "Email", email);
        }
        Carts cart = cartOpt.get();
        Long cartId = cart.getCart_id();
        CartDTO cartDTO=cartService.getCartByUser(email,cartId);
        return new ResponseEntity<>(cartDTO, HttpStatus.FOUND);
    }

    @PutMapping("/cart/product/{productId}/quantity/{operation}")
    public ResponseEntity<CartDTO> updateCartQuantity(
            @PathVariable Long productId,
            @PathVariable String operation) {

        int change;
        if (operation.equalsIgnoreCase("add")) {
            change = 1;
        } else if (operation.equalsIgnoreCase("delete")) {
            change = -1;
        } else {
            throw new APIException("Invalid operation. Use 'add' or 'delete'.");
        }

        CartDTO updatedCartDTO = cartService.updateCartQuantityInCart(productId, change);
        return ResponseEntity.ok(updatedCartDTO);
    }


    @DeleteMapping("/removeCart/product/{productId}/cart/{cartId}")
    public ResponseEntity<String> removeProductFromCart(@PathVariable Long productId ,@PathVariable Long cartId ){
        String status = cartService.deleteProductFormCart(productId, cartId);
        return new ResponseEntity<>(status ,HttpStatus.OK);
    }




}
