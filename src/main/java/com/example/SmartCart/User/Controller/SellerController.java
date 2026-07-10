package com.example.SmartCart.User.Controller;


import com.example.SmartCart.User.Dto.Seller.SellerProfileRequestDto;
import com.example.SmartCart.User.Dto.Seller.SellerProfileResponseDto;
import com.example.SmartCart.User.Service.SellerProfileService;
import com.example.SmartCart.common.Handler.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller/profile")
public class SellerController {

    private final SellerProfileService sellerProfileService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<SellerProfileResponseDto>> registerAsSeller(
            @Valid @RequestBody SellerProfileRequestDto request) {

        Long userId = 1L;
        SellerProfileResponseDto response = sellerProfileService.registerAsSeller(userId,request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Seller registration submitted successfully.",
                        response
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<SellerProfileResponseDto>> getSellerProfile() {
        Long userId = 1L;

        SellerProfileResponseDto response =
                sellerProfileService.getSellerProfile(userId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Seller profile fetched successfully.",
                        response
                )
        );
    }

    @PutMapping
    public ResponseEntity<ApiResponse<SellerProfileResponseDto>> updateSellerProfile(
            @Valid @RequestBody SellerProfileRequestDto request) {
        Long userId = 1L;

        SellerProfileResponseDto response =
                sellerProfileService.updateSellerProfile(userId, request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Seller profile updated successfully.",
                        response
                )
        );
    }
}
