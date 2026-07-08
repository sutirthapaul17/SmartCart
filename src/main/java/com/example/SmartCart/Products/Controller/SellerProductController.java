package com.example.SmartCart.Products.Controller;

import com.example.SmartCart.Products.Dto.RequestDto.CreateProductRequest;
import com.example.SmartCart.Products.Dto.RequestDto.UpdateProductRequest;
import com.example.SmartCart.Products.Dto.ResponseDto.ProductDeleteResponse;
import com.example.SmartCart.Products.Dto.ResponseDto.ProductResponse;
import com.example.SmartCart.Products.Dto.ResponseDto.SellerProductResponse;
import com.example.SmartCart.Products.Service.SellerProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seller/products")
@RequiredArgsConstructor
public class SellerProductController {

    private final SellerProductService sellerProductService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody CreateProductRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(sellerProductService.createProduct(request));
    }

    @GetMapping
    public ResponseEntity<Page<SellerProductResponse>> getSellerProducts(Pageable pageable) {

        return ResponseEntity.ok(
                sellerProductService.getSellerProducts(pageable));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductRequest request) {

        return ResponseEntity.ok(
                sellerProductService.updateProduct(productId, request));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ProductDeleteResponse> deleteProduct(
            @PathVariable Long productId) {

        return ResponseEntity.ok(
                sellerProductService.deleteProduct(productId));
    }
}
