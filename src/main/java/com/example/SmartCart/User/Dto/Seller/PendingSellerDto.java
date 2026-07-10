package com.example.SmartCart.User.Dto.Seller;

public record PendingSellerDto(
        Long sellerId,
        String fullName,
        String email,
        String businessName,
        String phone,
        String gstNumber,
        String panNumber
) {}
