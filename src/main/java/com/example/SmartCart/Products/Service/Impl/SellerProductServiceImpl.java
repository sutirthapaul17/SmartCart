package com.example.SmartCart.Products.Service.Impl;

import com.example.SmartCart.Products.Dto.RequestDto.CreateProductRequest;
import com.example.SmartCart.Products.Dto.RequestDto.UpdateProductRequest;
import com.example.SmartCart.Products.Dto.ResponseDto.ProductDeleteResponse;
import com.example.SmartCart.Products.Dto.ResponseDto.ProductResponse;
import com.example.SmartCart.Products.Dto.ResponseDto.SellerProductResponse;
import com.example.SmartCart.Products.Service.SellerProductService;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class SellerProductServiceImpl implements SellerProductService {
    @Override
    public @Nullable ProductResponse createProduct(CreateProductRequest request) {
        return null;
    }

    @Override
    public @Nullable Page<SellerProductResponse> getSellerProducts(Pageable pageable) {
        return null;
    }

    @Override
    public @Nullable ProductResponse updateProduct(Long productId, UpdateProductRequest request) {
        return null;
    }

    @Override
    public @Nullable ProductDeleteResponse deleteProduct(Long productId) {
        return null;
    }
}
