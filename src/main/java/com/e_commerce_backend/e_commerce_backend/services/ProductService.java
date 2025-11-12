package com.e_commerce_backend.e_commerce_backend.services;

import com.e_commerce_backend.e_commerce_backend.entity.Dto.ProductDTO;
import com.e_commerce_backend.e_commerce_backend.entity.dtoResponse.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {

    //create Product
    ProductDTO addProduct(ProductDTO product, Long categoryId);

    //Update Product
    //deleted Product
    //ListOfProduct
    ProductResponse getAllProduct(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);


    //getAllProductByCategory
   
    
    ProductResponse getAllProductByCategory(Long categoryId);

    ProductResponse searchProductByKeyword(String keyword);

    ProductDTO updateProduct(ProductDTO productDTO, Long productId);

    ProductDTO deleteProduct(Long productId);

    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;

    ProductResponse getAllProductSavedByUser(Long userId);

    //UpdateProductImage
    //getProductBySeller
    //getProductCount
}
