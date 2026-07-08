package com.example.SmartCart.User.Dto.User;

public record userUpdateDto(
        String firstName,
        String lastName,
        String phoneNumber,
        String dob,
        String gender
) {
}
