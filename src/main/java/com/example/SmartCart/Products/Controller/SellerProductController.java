package com.example.SmartCart.Products.Controller;

import com.example.SmartCart.Products.Dto.RequestDto.CreateProductRequestDto;
import com.example.SmartCart.Products.Dto.RequestDto.UpdateProductRequest;
import com.example.SmartCart.Products.Dto.ResponseDto.ProductDeleteResponse;
import com.example.SmartCart.Products.Dto.ResponseDto.ProductResponse;
import com.example.SmartCart.Products.Dto.ResponseDto.SellerProductResponse;
import com.example.SmartCart.Products.Service.SellerProductService;
import com.example.SmartCart.common.Handler.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seller/products")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('SELLER','CUSTOMER')")
public class SellerProductController {

    private final SellerProductService sellerProductService;

    @PostMapping()
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody CreateProductRequestDto request) {
        System.out.println("Creating product...");

        ProductResponse response = sellerProductService.createProduct(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<ProductResponse>builder()
                        .message("Product created successfully.")
                        .data(response)
                        .build());
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Page<SellerProductResponse>>> getSellerProducts(
            Pageable pageable) {

        Page<SellerProductResponse> response =
                sellerProductService.getSellerProducts(pageable);

        return ResponseEntity.ok(
                ApiResponse.<Page<SellerProductResponse>>builder()
                        .message("Products fetched successfully.")
                        .data(response)
                        .build());
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductRequest request) {

        ProductResponse response =
                sellerProductService.updateProduct(productId, request);

        return ResponseEntity.ok(
                ApiResponse.<ProductResponse>builder()
                        .message("Product updated successfully.")
                        .data(response)
                        .build());
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDeleteResponse>> deleteProduct(
            @PathVariable Long productId) {

        ProductDeleteResponse response =
                sellerProductService.deleteProduct(productId);

        return ResponseEntity.ok(
                ApiResponse.<ProductDeleteResponse>builder()
                        .message(response.message())
                        .data(response)
                        .build());
    }
}
