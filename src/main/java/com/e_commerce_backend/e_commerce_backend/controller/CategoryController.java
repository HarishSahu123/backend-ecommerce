package com.e_commerce_backend.e_commerce_backend.controller;

import com.e_commerce_backend.e_commerce_backend.entity.Dto.CategoryDTO;
import com.e_commerce_backend.e_commerce_backend.entity.dtoResponse.CategoryResponse;
import com.e_commerce_backend.e_commerce_backend.repository.CategoryRepository;
import com.e_commerce_backend.e_commerce_backend.serviceImp.CategoryServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryServiceImpl categoryService;

    public CategoryController(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }


    @PostMapping("/admin/saveCategory")
    public ResponseEntity<CategoryDTO> addCategory(@RequestBody CategoryDTO categoryDTO){
        CategoryDTO categoryDto= categoryService.addCategory(categoryDTO);
        return new ResponseEntity<>(categoryDto , HttpStatus.CREATED);
    }

    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategory( ){
        List<CategoryDTO> allCategory= categoryService.getAllCategory();
        CategoryResponse categoryResponse=new CategoryResponse();
        categoryResponse.setContent(allCategory);
        return new ResponseEntity<>(categoryResponse , HttpStatus.FOUND);
    }

    @GetMapping("/public/category/{categoryId}")
    public ResponseEntity<CategoryResponse> getAllCategory(@PathVariable  Long categoryId){
        CategoryResponse categoryDto= categoryService.getCategoryById(categoryId);
        return new ResponseEntity<>(categoryDto , HttpStatus.FOUND);
    }



}
