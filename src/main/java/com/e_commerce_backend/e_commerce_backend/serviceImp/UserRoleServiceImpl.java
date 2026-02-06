package com.e_commerce_backend.e_commerce_backend.serviceImp;

import com.e_commerce_backend.e_commerce_backend.Exception.APIException;
import com.e_commerce_backend.e_commerce_backend.entity.Dto.UserDTO;
import com.e_commerce_backend.e_commerce_backend.entity.RoleEntity;
import com.e_commerce_backend.e_commerce_backend.entity.UserEntity;
import com.e_commerce_backend.e_commerce_backend.entity.dtoResponse.UserResponseDTO;
import com.e_commerce_backend.e_commerce_backend.repository.RoleRepository;
import com.e_commerce_backend.e_commerce_backend.repository.UserRepository;
import com.e_commerce_backend.e_commerce_backend.services.UserRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {
    private static final Logger logger =
            LoggerFactory.getLogger(UserRoleServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


       @Override
       public UserResponseDTO createUser(UserDTO request) {

           // 1️⃣ Check email uniqueness
           if (userRepository.existsByEmail(request.getEmail())) {
               throw new APIException("Email already registered");
           }

            // 2️⃣ Fetch Role
            RoleEntity role = roleRepository.findById(request.getRoleId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Role not found with id: " + request.getRoleId()
                    ));

            // 3️⃣ Map DTO → Entity
            UserEntity user = new UserEntity();
            user.setEmail(request.getEmail());
            user.setUsername(request.getUsername());
            user.setRole(role);

            // 4️⃣ Encode password BEFORE save
            user.setPassword(passwordEncoder.encode(request.getPassword()));

            // 5️⃣ Save user
            UserEntity savedUser = userRepository.save(user);

            logger.info("User created successfully with id {}", savedUser.getId());

            // 6️⃣ Map Entity → Response DTO
            return UserResponseDTO.builder()
                    .id(savedUser.getId())
                    .email(savedUser.getEmail())
                    .username(savedUser.getUsername())
                    .roleId(role.getId())
                    .roleName(role.getRole())
                    .creationTimestamp(savedUser.getCreationTimestamp())
                    .updateTimestamp(savedUser.getUpdateTimestamp())
                    .build();
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
