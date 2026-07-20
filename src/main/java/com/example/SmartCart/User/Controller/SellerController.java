package com.example.SmartCart.User.Controller;


import com.example.SmartCart.User.Dto.Seller.SellerProfileRequestDto;
import com.example.SmartCart.User.Dto.Seller.SellerProfileResponseDto;
import com.example.SmartCart.User.Entity.User;
import com.example.SmartCart.User.Service.SellerProfileService;
import com.example.SmartCart.common.Handler.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller/profile")
@PreAuthorize("hasAnyRole('SELLER','CUSTOMER')")
public class SellerController {

    private final SellerProfileService sellerProfileService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<SellerProfileResponseDto>> registerAsSeller(
            @Valid @RequestBody SellerProfileRequestDto request) {

        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Long userId = user.getId();
        SellerProfileResponseDto response = sellerProfileService.registerAsSeller(userId,request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Seller registration submitted successfully.",
                        response
                )
        );
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<SellerProfileResponseDto>> getSellerProfile() {
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Long userId = user.getId();

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
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Long userId = user.getId();

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
