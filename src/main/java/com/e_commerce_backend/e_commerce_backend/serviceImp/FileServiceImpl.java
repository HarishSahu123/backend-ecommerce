package com.e_commerce_backend.e_commerce_backend.serviceImp;

import com.e_commerce_backend.e_commerce_backend.services.FIleService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FIleService {

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        //Step -1 -> file name of current /original file
        String originalFileName= file.getOriginalFilename();

        //step -2 -> generate unique file name for by UUID
        String randomId= UUID.randomUUID().toString();
        //Harish.png -> 1234.png where 1234 is randamId
        String newFilename=randomId.concat(originalFileName.substring(originalFileName.lastIndexOf(".")));

        String filepath=path + File.pathSeparator +newFilename;
//        path + "/" +filepath  filepath can be this but "/" is hardcoded so i am use pathseperateto


        //step-3-> path is exit or create
        File folder=new File(path);
        if(!folder.exists()){
            folder.mkdir();
        }
        //Step-3 -> upload the file to server
        Files.copy(file.getInputStream(), Paths.get(filepath));
        //Step-4 -> return file name
        return newFilename;

    }
}
