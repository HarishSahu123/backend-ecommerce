package com.e_commerce_backend.e_commerce_backend.controller;


import com.e_commerce_backend.e_commerce_backend.Utility.helperClass.AuthUtil;
import com.e_commerce_backend.e_commerce_backend.config.AppConstants;
import com.e_commerce_backend.e_commerce_backend.entity.Dto.ProductDTO;
import com.e_commerce_backend.e_commerce_backend.entity.dtoResponse.ProductResponse;
import com.e_commerce_backend.e_commerce_backend.serviceImp.ProductServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductServiceImpl productService;
    private final AuthUtil authUtil;

    public ProductController(ProductServiceImpl productService, AuthUtil authUtil) {
        this.productService = productService;
        this.authUtil = authUtil;
    }

    @PostMapping("/admin/category/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(
            @RequestBody ProductDTO product,
            @PathVariable Long categoryId) {
        ProductDTO productResponse = productService.addProduct(product, categoryId);
        return new ResponseEntity<>(productResponse ,HttpStatus.CREATED);
    }

    @GetMapping("/v1/public/products")
    public ResponseEntity<ProductResponse> getAllProduct(@RequestParam (name = "pageNumber" ,defaultValue = AppConstants.PAGE_NUMBER,required = false)  Integer pageNumber,
                                                         @RequestParam(name = "pageSize" ,defaultValue = AppConstants.PAGE_SIZE ,required = false) Integer pageSize,
                                                         @RequestParam(name = "sortBy" ,defaultValue = AppConstants.SORT_PRODUCT_BY ,required = false) String sortBy,
                                                         @RequestParam(name = "sortOrder" ,defaultValue = AppConstants.SORT_DIR ,required = false)  String sortOrder
                                                         ){
        ProductResponse productList= productService.getAllProduct(pageNumber, pageSize ,sortBy,sortOrder );
        return new ResponseEntity<>(productList,HttpStatus.OK);
    }

    @GetMapping("/public/category/{categoryId}/product")
    public ResponseEntity<ProductResponse> getAllProductByCategory(@PathVariable Long categoryId){
        ProductResponse productList=productService.getAllProductByCategory(categoryId);
        return new ResponseEntity<>(productList ,HttpStatus.OK);
    }

    @GetMapping("/public/product/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeywords(@PathVariable String keyword ){
       ProductResponse productResponse= productService.searchProductByKeyword(keyword);
        return new ResponseEntity<>(productResponse ,HttpStatus.OK);
    }

    @PutMapping("/admin/product/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO productDTO,
                                                    @PathVariable Long productId){
        ProductDTO productDto = productService.updateProduct(productDTO, productId);
        return new ResponseEntity<>(productDto,HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/product/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId){
        ProductDTO productDTO = productService.deleteProduct(productId);
        return new ResponseEntity<>(productDTO,HttpStatus.OK);
    }

    @PutMapping("/admin/product/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId ,
                                                         @RequestParam("Image")MultipartFile image) throws IOException {
        ProductDTO productDTO= productService.updateProductImage(productId ,image);
        return new ResponseEntity<>(productDTO ,HttpStatus.OK);
    }

    @GetMapping("/products")
    public ResponseEntity<ProductResponse> getAllProductSavedByUser(){
        Long userId = authUtil.loggedInUserId();
        ProductResponse products = productService.getAllProductSavedByUser(userId);
        return new ResponseEntity<>(products ,HttpStatus.OK);
    }

    @GetMapping("/v1/public/products/image/{fileName}")
    public ResponseEntity<Resource> getImage(@PathVariable String fileName) {
        Resource resource = productService.getProductImage(fileName);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)  // ⚠ IMPORTANT
                .body(resource);
    }



}
