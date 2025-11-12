package com.e_commerce_backend.e_commerce_backend.repository;

import com.e_commerce_backend.e_commerce_backend.entity.Category;
import com.e_commerce_backend.e_commerce_backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product ,Long> {
    List<Product> findByCategoryOrderByPriceAsc(Category category);

   @Query
    List<Product> findByProductNameLikeIgnoreCase(String keyword);

 //  @Query("SELECT p FROM Product where p.userId=:userId")
    List<Product> findByUserId(Long userId);
}
