package com.example.SmartCart.User.Dto.User;

import com.example.SmartCart.User.enums.Gender;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UserUpdateDto(
        @Size(min = 2, max = 50,
                message = "First name must be between 2 and 50 characters.")
        String firstName,
        @Size(min = 2, max = 50,
                message = "Last name must be between 2 and 50 characters.")
        String lastName,
        @Pattern(
                regexp = "^[6-9]\\d{9}$",
                message = "Phone number must be a valid 10-digit Indian mobile number."
        )
        String phoneNumber,
        @Past(message = "Date of birth must be in the past.")
        LocalDate dob,
        Gender gender
) {
}
