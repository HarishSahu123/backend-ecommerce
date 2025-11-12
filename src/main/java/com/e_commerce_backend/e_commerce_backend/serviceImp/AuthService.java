package com.e_commerce_backend.e_commerce_backend.serviceImp;

import com.e_commerce_backend.e_commerce_backend.Utility.JwtUtil;
import com.e_commerce_backend.e_commerce_backend.entity.Request.LoginRequestdto;
import com.e_commerce_backend.e_commerce_backend.entity.RoleEntity;
import com.e_commerce_backend.e_commerce_backend.entity.UserEntity;
import com.e_commerce_backend.e_commerce_backend.entity.dtoResponse.LoginResponsedto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Collections; // ✅ For single-element list
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponsedto login(LoginRequestdto loginRequestdto) {

        // ✅ Step 1: Authenticate the credentials
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestdto.getUsername(),
                        loginRequestdto.getPassword()
                )
        );

        // ✅ Step 2: Extract authenticated user
        UserEntity user = (UserEntity) authentication.getPrincipal();

        // ✅ Step 3: Get the single role of this user
        RoleEntity role = user.getRole();

        // ✅ Step 4: Generate JWT — wrap single role in a list
        String token = jwtUtil.generateToken(user.getUsername(), Collections.singletonList(role));

        // ✅ Step 5: Prepare and return response
        LoginResponsedto response = new LoginResponsedto();
        response.setUsername(user.getUsername());
        response.setJwt(token);
        response.setMessage("Login successful");
        response.setStatus(true);
        response.setRoles(
                Collections.singletonList(role.getRole()) // send single role as list
        );

        return response;
    }
}
