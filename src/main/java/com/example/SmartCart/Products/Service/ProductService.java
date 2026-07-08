package com.example.SmartCart.Products.Service;

import com.example.SmartCart.Products.Dto.RequestDto.ProductSearchRequest;
import com.example.SmartCart.Products.Dto.ResponseDto.ProductDetailsResponse;
import com.example.SmartCart.Products.Dto.ResponseDto.ProductResponse;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    @Nullable Page<ProductResponse> getProducts(Pageable pageable, ProductSearchRequest searchRequest);


    @Nullable ProductDetailsResponse getProduct(Long productId);

    @Nullable Page<ProductResponse> searchProducts(String keyword, Pageable pageable, ProductSearchRequest searchRequest);

}
