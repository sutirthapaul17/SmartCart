package com.example.SmartCart.Products.Controller;

import com.example.SmartCart.Products.Dto.RequestDto.CategoryRequestDto;
import com.example.SmartCart.Products.Dto.ResponseDto.CategoryResponse;
import com.example.SmartCart.Products.Service.CategoryService;
import com.example.SmartCart.common.Handler.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CategoryRequestDto dto) {

        CategoryResponse response = categoryService.createCategory(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<CategoryResponse>builder()
                        .message("Category created successfully.")
                        .data(response)
                        .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {

        return ResponseEntity.ok(ApiResponse.<List<CategoryResponse>>builder()
                .message("Category fetched successfully")
                .data(categoryService.getAllCategories())
                .build()
        );
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(
            @PathVariable Long categoryId) {

        return ResponseEntity.ok(
                ApiResponse.<CategoryResponse>builder()
                        .message("Category fetched successfully with id "+categoryId)
                        .data(categoryService.getCategoryById(categoryId))
                        .build()
        );
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable Long categoryId,
            @Valid @RequestBody CategoryRequestDto dto) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                    ApiResponse.<CategoryResponse>builder()
                            .message("Category updated")
                            .data(categoryService.updateCategory(categoryId, dto))
                            .build());
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(
            @PathVariable Long categoryId) {

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .message("Category deleted")
                        .build()
        );
    }
}
