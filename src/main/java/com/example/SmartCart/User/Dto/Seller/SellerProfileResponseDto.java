package com.example.SmartCart.User.Dto.Seller;

import com.example.SmartCart.User.enums.SellerStatus;

public record SellerProfileResponseDto(

        Long id,

        String storeName,

        String description,

        String gstNumber,

        SellerStatus status
) {
}