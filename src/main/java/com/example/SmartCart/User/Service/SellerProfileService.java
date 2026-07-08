package com.example.SmartCart.User.Service;

import com.example.SmartCart.User.Dto.Seller.SellerProfileRequestDto;
import com.example.SmartCart.User.Dto.Seller.SellerProfileResponseDto;

public interface SellerProfileService {
    SellerProfileResponseDto getSellerProfile();

    SellerProfileResponseDto updateSellerProfile(
            SellerProfileRequestDto request
    );
}
