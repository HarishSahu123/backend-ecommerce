package com.e_commerce_backend.e_commerce_backend.serviceImp;

import com.e_commerce_backend.e_commerce_backend.entity.RoleEntity;
import com.e_commerce_backend.e_commerce_backend.entity.UserEntity;
import com.e_commerce_backend.e_commerce_backend.repository.RoleRepository;
import com.e_commerce_backend.e_commerce_backend.repository.UserRepository;
import com.e_commerce_backend.e_commerce_backend.services.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserEntity createUser(UserEntity user) {
        // Optional: Check if role exists, otherwise throw an exception
        if(user.getRole()!=null){
            RoleEntity role=roleRepository.findById(user.getRole().getId()).orElseThrow(
                    ()->new RuntimeException("Role not found :"+user.getRole().getId()));
            user.setRole(role);
        }else {
           throw new RuntimeException("User must have a role assigned");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public List<UserEntity> listOfUsers() {
        return null;
    }

    @Override
    public UserEntity getUserDetail(int id) {
        return null;
    }

    @Override
    public void deleteUser(int id) {

    }
}
