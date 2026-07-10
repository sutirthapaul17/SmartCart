package com.example.SmartCart.Products.Service.Impl;

import com.example.SmartCart.Products.Dto.RequestDto.ProductSearchRequest;
import com.example.SmartCart.Products.Dto.ResponseDto.ProductDetailsResponse;
import com.example.SmartCart.Products.Dto.ResponseDto.ProductResponse;
import com.example.SmartCart.Products.Service.ProductService;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class ProductServiceImpl implements ProductService {
    @Override
    public @Nullable Page<ProductResponse> getProducts(Pageable pageable, ProductSearchRequest searchRequest) {
        return null;
    }

    @Override
    public @Nullable ProductDetailsResponse getProduct(Long productId) {
        return null;
    }

    @Override
    public @Nullable Page<ProductResponse> searchProducts(String keyword, Pageable pageable, ProductSearchRequest searchRequest) {
        return null;
    }
}
