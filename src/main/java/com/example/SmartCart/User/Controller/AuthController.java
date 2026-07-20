package com.example.SmartCart.User.Controller;


import com.example.SmartCart.User.Dto.Auth.LoginRequest;
import com.example.SmartCart.User.Dto.Auth.LoginResponse;
import com.example.SmartCart.User.Dto.User.CreateUserDto;
import com.example.SmartCart.User.Service.AuthService;
import com.example.SmartCart.common.Handler.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(
                ApiResponse.<LoginResponse>builder()
                        .message("Login successful")
                        .data(response)
                        .build()
        );
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<LoginResponse>> register(
            @Valid @RequestBody CreateUserDto request) {

        LoginResponse response = authService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<LoginResponse>builder()
                                .message("Registration successful")
                                .data(response)
                                .build()
                );
    }
}
