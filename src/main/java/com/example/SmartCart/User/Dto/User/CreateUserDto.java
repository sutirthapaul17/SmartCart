package com.example.SmartCart.User.Dto.User;

import com.example.SmartCart.User.Dto.Address.AddressRequestDto;
import com.example.SmartCart.User.Entity.Address;
import com.example.SmartCart.User.enums.Gender;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public record CreateUserDto(
        @NotBlank(message = "First name is required.")
        @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters.")
        String firstName,

        @NotBlank(message = "Last name is required.")
        @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters.")
        String lastName,

        @NotBlank(message = "Email is required.")
        @Email(message = "Invalid Email Format")
        String email,

        @NotBlank(message = "Phone number is required.")
        @Pattern(
                regexp = "^[6-9]\\d{9}$",
                message = "Phone number must be a valid 10-digit Indian mobile number."
        )
        String phoneNumber,

        @NotBlank(message = "Password is required.")
        @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters.")
        String password,

        @NotNull(message = "Date of birth is required.")
        @Past(message = "Date of birth must be in the past.")
        LocalDate dateOfBirth,

        @NotNull(message = "Gender is required.")
        Gender gender,

        @NotEmpty(message = "At least one address is required.")
        @Valid
        List<AddressRequestDto> addresses
) {
}
