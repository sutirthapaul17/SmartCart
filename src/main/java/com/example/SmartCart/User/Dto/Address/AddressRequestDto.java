package com.example.SmartCart.User.Dto.Address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddressRequestDto(

        @Size(max = 50)
        String addressLabel,

        @NotBlank(message = "Address line 1 is required")
        @Size(max = 255)
        String addressLine1,

        @Size(max = 255)
        String addressLine2,

        @NotBlank(message = "City is required")
        @Size(max = 100)
        String city,

        @NotBlank(message = "State is required")
        @Size(max = 100)
        String state,

        @NotBlank(message = "Country is required")
        @Size(max = 100)
        String country,

        @NotBlank(message = "Postal code is required")
        @Pattern(
                regexp = "^[A-Za-z0-9\\- ]{3,10}$",
                message = "Invalid postal code"
        )
        String postalCode,

        Boolean defaultAddress

) {}