package com.example.SmartCart.Mapper.Product;

import com.example.SmartCart.Products.Dto.RequestDto.CategoryRequestDto;
import com.example.SmartCart.Products.Dto.ResponseDto.CategoryResponse;
import com.example.SmartCart.Products.Entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toEntity(CategoryRequestDto dto);

    CategoryResponse toResponse(Category category);

    void updateEntity(CategoryRequestDto dto,
                      @MappingTarget Category category);
}
