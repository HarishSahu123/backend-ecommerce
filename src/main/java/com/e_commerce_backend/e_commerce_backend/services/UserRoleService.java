package com.e_commerce_backend.e_commerce_backend.services;

import com.e_commerce_backend.e_commerce_backend.entity.Dto.UserDTO;
import com.e_commerce_backend.e_commerce_backend.entity.UserEntity;
import com.e_commerce_backend.e_commerce_backend.entity.dtoResponse.UserResponseDTO;
import org.apache.catalina.User;

import java.util.List;

public interface UserRoleService {

    public UserResponseDTO createUser(UserDTO request);

    List<UserEntity> listOfUsers();

    UserEntity getUserDetail(int id);

    void deleteUser(int id);

}
