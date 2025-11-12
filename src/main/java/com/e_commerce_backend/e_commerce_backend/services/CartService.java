package com.e_commerce_backend.e_commerce_backend.services;

import com.e_commerce_backend.e_commerce_backend.entity.Dto.CartDTO;

import java.util.List;

public interface CartService {
    CartDTO addProductTOcart(Long productId, Integer quantity);

    List<CartDTO> getAllCarts();

    CartDTO getCartByUser(String email, Long cartId);


    CartDTO updateCartQuantityInCart(Long productId, Integer quality);

    String deleteProductFormCart(Long productId, Long cartId);
}
