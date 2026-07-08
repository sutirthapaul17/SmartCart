package com.example.SmartCart.User.Dto.User;

import com.example.SmartCart.User.enums.Gender;
import com.example.SmartCart.User.enums.UserRole;
import com.example.SmartCart.User.enums.UserStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserResponseDto(

        Long id,

        String firstName,

        String lastName,

        String email,

        String phoneNumber,

        UserRole role,

        Gender gender,

        LocalDate dateOfBirth,

        UserStatus status,

        Boolean emailVerified,

        LocalDateTime lastLoginAt,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}
