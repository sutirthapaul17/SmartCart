package com.example.SmartCart.Mapper.Product;

import com.example.SmartCart.Products.Dto.RequestDto.CreateProductRequestDto;
import com.example.SmartCart.Products.Dto.RequestDto.UpdateProductRequest;
import com.example.SmartCart.Products.Dto.ResponseDto.ProductResponse;
import com.example.SmartCart.Products.Dto.ResponseDto.SellerProductResponse;
import com.example.SmartCart.Products.Entity.Product;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true)
)
public interface SellerProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "seller", ignore = true)
    @Mapping(target = "category", ignore = true)
    Product toEntity(CreateProductRequestDto dto);

    @Mapping(target = "category", source = "category.name")
    @Mapping(target = "active", source = "status")
    ProductResponse toProductResponse(Product product);

    SellerProductResponse toSellerProductResponse(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "seller", ignore = true)
    @Mapping(target = "category", ignore = true)
    void updateProductFromDto(UpdateProductRequest dto,
                              @MappingTarget Product product);
}