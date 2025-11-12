package com.e_commerce_backend.e_commerce_backend.services;

import com.e_commerce_backend.e_commerce_backend.entity.Dto.CategoryDTO;
import com.e_commerce_backend.e_commerce_backend.entity.dtoResponse.CategoryResponse;

import java.util.List;

public interface CategoryService {
     CategoryDTO addCategory(CategoryDTO categoryDTO);

     CategoryResponse getCategoryById(Long categoryId);

     List<CategoryDTO> getAllCategory();
}
