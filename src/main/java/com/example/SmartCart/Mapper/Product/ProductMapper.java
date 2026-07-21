package com.example.SmartCart.Mapper.Product;

import com.example.SmartCart.Products.Dto.RequestDto.CreateProductRequestDto;
import com.example.SmartCart.Products.Dto.ResponseDto.ProductDetailsResponse;
import com.example.SmartCart.Products.Dto.ResponseDto.ProductResponse;
import com.example.SmartCart.Products.Entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(CreateProductRequestDto dto);

    @Mapping(target = "thumbnail",
            expression = "java(getThumbnail(product))")
    @Mapping(target = "category",
            source = "category.name")
    @Mapping(target = "active",
            source = "status")
    ProductResponse toResponse(Product product);

    @Mapping(target = "seller",
            source = "seller.storeName")
    @Mapping(target = "category",
            source = "category.name")
    @Mapping(target = "images",
            expression = "java(product.getImages().stream().map(i -> i.getImageUrl()).toList())")
    ProductDetailsResponse toDetails(Product product);

    default String getThumbnail(Product product) {

        return product.getImages()
                .stream()
                .filter(image -> Boolean.TRUE.equals(image.getPrimaryImage()))
                .findFirst()
                .map(image -> image.getImageUrl())
                .orElse(null);
    }
}