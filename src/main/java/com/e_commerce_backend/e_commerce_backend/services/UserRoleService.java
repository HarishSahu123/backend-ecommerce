package com.e_commerce_backend.e_commerce_backend.services;

import com.e_commerce_backend.e_commerce_backend.entity.UserEntity;
import org.apache.catalina.User;

import java.util.List;

public interface UserRoleService {

    UserEntity createUser(UserEntity user);

    List<UserEntity> listOfUsers();

    UserEntity getUserDetail(int id);

    void deleteUser(int id);

}
