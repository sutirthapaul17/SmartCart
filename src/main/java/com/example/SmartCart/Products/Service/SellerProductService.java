package com.example.SmartCart.Products.Service;

import com.example.SmartCart.Products.Dto.RequestDto.CreateProductRequest;
import com.example.SmartCart.Products.Dto.RequestDto.UpdateProductRequest;
import com.example.SmartCart.Products.Dto.ResponseDto.ProductDeleteResponse;
import com.example.SmartCart.Products.Dto.ResponseDto.ProductResponse;
import com.example.SmartCart.Products.Dto.ResponseDto.SellerProductResponse;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SellerProductService {
    @Nullable ProductResponse createProduct(@Valid CreateProductRequest request);


    @Nullable Page<SellerProductResponse> getSellerProducts(Pageable pageable);

    @Nullable ProductResponse updateProduct(Long productId, @Valid UpdateProductRequest request);

    @Nullable ProductDeleteResponse deleteProduct(Long productId);

}
