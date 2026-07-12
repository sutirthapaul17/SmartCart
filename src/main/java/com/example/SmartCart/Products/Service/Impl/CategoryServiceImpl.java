package com.example.SmartCart.Products.Service.Impl;

import com.example.SmartCart.Mapper.Product.CategoryMapper;
import com.example.SmartCart.Products.Dto.RequestDto.CategoryRequestDto;
import com.example.SmartCart.Products.Dto.ResponseDto.CategoryResponse;
import com.example.SmartCart.Products.Entity.Category;
import com.example.SmartCart.Products.Repository.CategoryRepository;
import com.example.SmartCart.Products.Service.CategoryService;
import com.example.SmartCart.common.Exception.ConflictException;
import com.example.SmartCart.common.Exception.ResourceNotFoundException;
import com.example.SmartCart.common.Handler.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse createCategory(CategoryRequestDto dto) {

        if (categoryRepository.existsByNameIgnoreCase(dto.name())) {
            throw new ConflictException("Category already exists.");
        }

        Category category = categoryMapper.toEntity(dto);
        category.setActive(true);

        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.toResponse(savedCategory);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {

        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }



    @Override
    public CategoryResponse getCategoryById(Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: " + categoryId));

        return categoryMapper.toResponse(category);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Long categoryId,
                                           CategoryRequestDto dto) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: " + categoryId));

        if (!category.getName().equalsIgnoreCase(dto.name())
                && categoryRepository.existsByNameIgnoreCase(dto.name())) {

            throw new ConflictException("Category already exists.");
        }

        categoryMapper.updateEntity(dto, category);

        Category updatedCategory = categoryRepository.save(category);

        return categoryMapper.toResponse(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: " + categoryId));

        category.setActive(false);
    }
}
