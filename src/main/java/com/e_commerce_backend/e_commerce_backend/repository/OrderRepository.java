package com.e_commerce_backend.e_commerce_backend.repository;

import com.e_commerce_backend.e_commerce_backend.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders , Long> {


    @Query(
            value = "SELECT * FROM orders WHERE user_id = :userId",
            nativeQuery = true
    )
    List<Orders> findOrdersByUserId(@Param("userId") Long userId);
}
