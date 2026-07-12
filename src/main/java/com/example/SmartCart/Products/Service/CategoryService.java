package com.example.SmartCart.Products.Service;

import com.example.SmartCart.Products.Dto.RequestDto.CategoryRequestDto;
import com.example.SmartCart.Products.Dto.ResponseDto.CategoryResponse;
import com.example.SmartCart.common.Handler.ApiResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse createCategory(CategoryRequestDto dto);

    List<CategoryResponse> getAllCategories();

    CategoryResponse getCategoryById(Long categoryId);

    CategoryResponse updateCategory(Long categoryId,
                                    CategoryRequestDto dto);

    void deleteCategory(Long categoryId);
}
