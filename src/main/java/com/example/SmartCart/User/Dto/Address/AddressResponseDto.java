package com.example.SmartCart.User.Dto.Address;

public record AddressResponseDto(
        Long id,
        String addressLabel,
        String fullName,
        String phoneNumber,
        String addressLine1,
        String addressLine2,
        String city,
        String state,
        String country,
        String postalCode,
        Boolean defaultAddress
) {}