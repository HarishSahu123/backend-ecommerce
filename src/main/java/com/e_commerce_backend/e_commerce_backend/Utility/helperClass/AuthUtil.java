package com.e_commerce_backend.e_commerce_backend.Utility.helperClass;

import com.e_commerce_backend.e_commerce_backend.entity.UserEntity;
import com.e_commerce_backend.e_commerce_backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    private final Logger logger= LoggerFactory.getLogger(AuthUtil.class);

    private final UserRepository userRepository;

    public AuthUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String loggedInEmail(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        logger.info("authentication: " +authentication);
        UserEntity user = userRepository.findByUsername(authentication.getName()).orElseThrow(
                () -> new UsernameNotFoundException("username not found"));
        logger.info("user:" +user);
        return user.getEmail();
    }

    public Long loggedInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("authentication: " +authentication);
        UserEntity user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));
        logger.info("user:" +user.getUsername());
        return user.getId();
    }
}
