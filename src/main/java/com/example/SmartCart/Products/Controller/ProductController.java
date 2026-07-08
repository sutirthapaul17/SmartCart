package com.example.SmartCart.Products.Controller;

import com.example.SmartCart.Products.Dto.RequestDto.ProductSearchRequest;
import com.example.SmartCart.Products.Dto.ResponseDto.ProductDetailsResponse;
import com.example.SmartCart.Products.Dto.ResponseDto.ProductResponse;
import com.example.SmartCart.Products.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
             Pageable pageable,
            ProductSearchRequest searchRequest) {

        return ResponseEntity.ok(productService.getProducts(pageable, searchRequest));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDetailsResponse> getProductById(
            @PathVariable Long productId) {

        return ResponseEntity.ok(productService.getProduct(productId));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponse>> searchProducts(
            @RequestParam String keyword,
            Pageable pageable,
            ProductSearchRequest searchRequest) {

        return ResponseEntity.ok(
                productService.searchProducts(keyword, pageable, searchRequest));
    }
}
