package com.e_commerce_backend.e_commerce_backend.repository;

import com.e_commerce_backend.e_commerce_backend.entity.Carts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Carts ,Long> {
    @Query("SELECT c FROM Carts c WHERE c.user.email=?1")
    Optional<Carts> findCartByUsername(String email);

//    @Query("SELECT c FROM Carts where c.user.email = :email AND c.id=:cartId")
//    Carts findCartByEmailAndCartId(String email, Long cartId);

    @Query("SELECT c FROM Carts c WHERE c.user.email = :email AND c.cart_id = :cartId")
    Carts findCartByEmailAndCartId(@Param("email") String email, @Param("cartId") Long cartId);

}
