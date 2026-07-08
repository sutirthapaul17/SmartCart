package com.example.SmartCart.Products.Service;

import com.example.SmartCart.Products.Dto.ResponseDto.ProductDeleteResponse;
import com.example.SmartCart.Products.Dto.ResponseDto.ProductResponse;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminProductService {
    @Nullable Page<ProductResponse> getAllProducts(Pageable pageable);

    @Nullable ProductDeleteResponse deleteProduct(Long productId);
}


