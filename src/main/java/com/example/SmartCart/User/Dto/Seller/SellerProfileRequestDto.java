package com.example.SmartCart.User.Dto.Seller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SellerProfileRequestDto(

        @NotBlank(message = "Store name is required")
        @Size(max = 100)
        String storeName,

        @Size(max = 1000)
        String description,

        @NotBlank(message = "GST number is required")
        @Size(max = 20)
        String gstNumber,
        String panNumber) {
}
