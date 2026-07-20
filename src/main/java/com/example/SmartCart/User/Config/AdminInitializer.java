package com.example.SmartCart.User.Config;

import com.example.SmartCart.User.Entity.User;
import com.example.SmartCart.User.Repository.UserRepo;
import com.example.SmartCart.User.enums.UserRole;
import com.example.SmartCart.User.enums.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements ApplicationRunner {

    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        createAdmin(
                "Admin",
                "One",
                "admin1@smartcart.com",
                "9876543215",
                "Admin@123"
        );

        createAdmin(
                "Admin",
                "Two",
                "admin2@smartcart.com",
                "8596321478",
                "Admin@123"
        );
    }

    private void createAdmin(
            String firstName,
            String lastName,
            String email,
            String phone,
            String password) {

        if (userRepository.existsByEmail(email)) {
            return;
        }

        User admin = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phone)
                .password(passwordEncoder.encode(password))
                .role(UserRole.ADMIN)
                .status(UserStatus.ACTIVE)
                .emailVerified(true)
                .deleted(false)
                .build();

        userRepository.save(admin);
    }
}
