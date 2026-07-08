package com.example.SmartCart.User.Dto.Seller;

public record SellerProfileResponseDto(

        Long id,

        String storeName,

        String description,

        String gstNumber,

        boolean verified
) {
}