package com.example.SmartCart.User.Service;

import com.example.SmartCart.User.Dto.Seller.SellerProfileRequestDto;
import com.example.SmartCart.User.Dto.Seller.SellerProfileResponseDto;
import com.example.SmartCart.common.Handler.ApiResponse;
import jakarta.validation.Valid;

public interface SellerProfileService {
    SellerProfileResponseDto getSellerProfile(Long userId);

    SellerProfileResponseDto updateSellerProfile(
            Long userId, SellerProfileRequestDto request
    );

    SellerProfileResponseDto registerAsSeller(Long userId, @Valid SellerProfileRequestDto request);
}
