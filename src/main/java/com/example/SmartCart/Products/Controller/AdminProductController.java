package com.example.SmartCart.Products.Controller;

import com.example.SmartCart.Products.Dto.ResponseDto.ProductDeleteResponse;
import com.example.SmartCart.Products.Dto.ResponseDto.ProductResponse;
import com.example.SmartCart.Products.Service.AdminProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final AdminProductService adminProductService;

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            Pageable pageable) {

        return ResponseEntity.ok(
                adminProductService.getAllProducts(pageable));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ProductDeleteResponse> deleteProduct(
            @PathVariable Long productId) {

        return ResponseEntity.ok(
                adminProductService.deleteProduct(productId));
    }
}
