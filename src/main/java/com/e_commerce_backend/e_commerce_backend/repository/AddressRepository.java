package com.e_commerce_backend.e_commerce_backend.repository;

import com.e_commerce_backend.e_commerce_backend.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address ,Long> {

    @Query(
            value = "SELECT * FROM `ecommerce-backend`.address WHERE user_id = :userId",
            nativeQuery = true
    )
    List<Address> findAddressesByUserId(@Param("userId") Long userId);
}
