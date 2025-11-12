package com.e_commerce_backend.e_commerce_backend.repository;

import com.e_commerce_backend.e_commerce_backend.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    @Query("SELECT ci FROM CartItem ci WHERE ci.product.id = :productId AND ci.cart.id = :cartId")
    Optional<CartItem> findCartItemByProductIdAndCartId(Long productId, Long cartId);

   @Transactional
   @Modifying
    @Query("DELETE FROM CartItem ci where ci.product.id = :productId AND ci.cart.id = :cartId")
    void deleteCartItemByProductIdAndCartId(Long productId, Long cartId);
}
