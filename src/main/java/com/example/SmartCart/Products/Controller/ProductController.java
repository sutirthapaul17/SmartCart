package com.example.SmartCart.Products.Controller;

import com.example.SmartCart.Products.Dto.RequestDto.CreateProductRequestDto;
import com.example.SmartCart.Products.Dto.RequestDto.ProductSearchRequest;
import com.example.SmartCart.Products.Dto.ResponseDto.ProductDeleteResponse;
import com.example.SmartCart.Products.Dto.ResponseDto.ProductDetailsResponse;
import com.example.SmartCart.Products.Dto.ResponseDto.ProductResponse;
import com.example.SmartCart.Products.Service.ProductService;
import com.example.SmartCart.common.Handler.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody CreateProductRequestDto dto) {

        ProductResponse response = productService.createProduct(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<ProductResponse>builder()
                        .message("Product created successfully.")
                        .data(response)
                        .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getAllProducts(
            Pageable pageable,
            @ModelAttribute ProductSearchRequest searchRequest) {

        Page<ProductResponse> products = productService.getProducts(pageable, searchRequest);

        return ResponseEntity.ok(
                ApiResponse.<Page<ProductResponse>>builder()
                        .message("Products fetched successfully.")
                        .data(products)
                        .build()
        );
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDetailsResponse>> getProductById(
            @PathVariable Long productId) {

        ProductDetailsResponse product = productService.getProductById(productId);

        return ResponseEntity.ok(
                ApiResponse.<ProductDetailsResponse>builder()
                        .message("Product fetched successfully.")
                        .data(product)
                        .build()
        );
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> searchProducts(
            @RequestParam String keyword,
            Pageable pageable,
            @ModelAttribute ProductSearchRequest searchRequest) {

        Page<ProductResponse> products =
                productService.searchProducts(keyword, pageable, searchRequest);

        return ResponseEntity.ok(
                ApiResponse.<Page<ProductResponse>>builder()
                        .message("Search completed successfully.")
                        .data(products)
                        .build()
        );
    }


    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDeleteResponse>> deleteProduct(
            @PathVariable Long productId) {

        ProductDeleteResponse response = productService.delProduct(productId);

        return ResponseEntity.ok(
                ApiResponse.<ProductDeleteResponse>builder()
                        .message(response.message())
                        .data(response)
                        .build()
        );
    }


}
