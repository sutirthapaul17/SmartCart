package com.example.SmartCart.Products.Service;

import com.example.SmartCart.Products.Dto.RequestDto.CreateProductRequestDto;
import com.example.SmartCart.Products.Dto.RequestDto.ProductSearchRequest;
import com.example.SmartCart.Products.Dto.ResponseDto.ProductDeleteResponse;
import com.example.SmartCart.Products.Dto.ResponseDto.ProductDetailsResponse;
import com.example.SmartCart.Products.Dto.ResponseDto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Page<ProductResponse> getProducts(Pageable pageable, ProductSearchRequest searchRequest);


    ProductDetailsResponse getProductById(Long productId);

    Page<ProductResponse> searchProducts(String keyword, Pageable pageable, ProductSearchRequest searchRequest);

    ProductResponse createProduct(CreateProductRequestDto dto);

    ProductDeleteResponse delProduct(Long productId);

}
