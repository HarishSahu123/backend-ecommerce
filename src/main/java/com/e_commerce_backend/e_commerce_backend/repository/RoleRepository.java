package com.e_commerce_backend.e_commerce_backend.repository;

import com.e_commerce_backend.e_commerce_backend.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {

}
