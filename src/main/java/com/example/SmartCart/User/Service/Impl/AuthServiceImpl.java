package com.example.SmartCart.User.Service.Impl;

import com.example.SmartCart.User.Dto.Auth.LoginRequest;
import com.example.SmartCart.User.Dto.Auth.LoginResponse;
import com.example.SmartCart.User.Dto.User.CreateUserDto;
import com.example.SmartCart.User.Entity.User;
import com.example.SmartCart.User.Security.AuthUtil;
import com.example.SmartCart.User.Service.AuthService;
import com.example.SmartCart.User.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final AuthUtil jwtService;
    private final UserService userService;

    @Override
    public LoginResponse login(LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.email(),
                                request.password()
                        )
                );

        User user = (User) authentication.getPrincipal();

        String token = jwtService.generateToken(user);

        return new LoginResponse(
                token,
                "Bearer"
        );
    }

    @Override
    public LoginResponse register(CreateUserDto request) {

        userService.createUser(request);

        return login(
                new LoginRequest(
                        request.email(),
                        request.password()
                )
        );
    }
}
