package com.e_commerce_backend.e_commerce_backend.entity.dtoResponse;

import com.e_commerce_backend.e_commerce_backend.entity.Dto.AddressDTO;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Builder
@Data
public class UserResponseDTO {
    private Long id;
    private String email;
    private String username;

    // Role info (flattened, no RoleEntity exposure)
    private int roleId;
    private String roleName;
    private Instant creationTimestamp;
    private Instant updateTimestamp;



}
