package com.e_commerce_backend.e_commerce_backend.serviceImp;

import com.e_commerce_backend.e_commerce_backend.entity.Category;
import com.e_commerce_backend.e_commerce_backend.entity.Dto.CategoryDTO;
import com.e_commerce_backend.e_commerce_backend.entity.dtoResponse.CategoryResponse;
import com.e_commerce_backend.e_commerce_backend.repository.CategoryRepository;
import com.e_commerce_backend.e_commerce_backend.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final ModelMapper modelMapper;

    private final CategoryRepository  categoryRepository;

    public CategoryServiceImpl(ModelMapper modelMapper, CategoryRepository categoryRepository) {
        this.modelMapper = modelMapper;
        this.categoryRepository = categoryRepository;
    }


    @Override
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory ,CategoryDTO.class);
    }

    @Override
    public CategoryResponse getCategoryById(Long categoryId) {
        Optional<Category> categoryDb = categoryRepository.findById(categoryId);

        List<CategoryDTO> ListOfCategoryDto = categoryDb.stream().map(category -> modelMapper.map(categoryDb, CategoryDTO.class))
                .collect(Collectors.toList());
        CategoryResponse categoryResponse=new CategoryResponse();
        categoryResponse.setContent(ListOfCategoryDto);
        return categoryResponse;
    }

    @Override
    public List<CategoryDTO> getAllCategory() {

        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> categoriesDto = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());

        return categoriesDto;
    }

}
