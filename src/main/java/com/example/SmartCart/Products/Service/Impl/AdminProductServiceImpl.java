package com.example.SmartCart.Products.Service.Impl;

import com.example.SmartCart.Products.Dto.ResponseDto.ProductDeleteResponse;
import com.example.SmartCart.Products.Dto.ResponseDto.ProductResponse;
import com.example.SmartCart.Products.Service.AdminProductService;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class AdminProductServiceImpl implements AdminProductService {
    @Override
    public @Nullable Page<ProductResponse> getAllProducts(Pageable pageable) {
        return null;
    }

    @Override
    public @Nullable ProductDeleteResponse deleteProduct(Long productId) {
        return null;
    }
}
