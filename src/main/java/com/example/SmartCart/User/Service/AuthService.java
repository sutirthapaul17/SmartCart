package com.example.SmartCart.User.Service;

import com.example.SmartCart.User.Dto.Auth.LoginRequest;
import com.example.SmartCart.User.Dto.Auth.LoginResponse;
import com.example.SmartCart.User.Dto.User.CreateUserDto;

public interface AuthService {
    LoginResponse login(LoginRequest request);

    LoginResponse register(CreateUserDto request);
}
