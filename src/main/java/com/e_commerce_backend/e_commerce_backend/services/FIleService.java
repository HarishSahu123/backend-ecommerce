package com.e_commerce_backend.e_commerce_backend.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FIleService {

     String uploadImage(String path, MultipartFile file) throws IOException;
}
