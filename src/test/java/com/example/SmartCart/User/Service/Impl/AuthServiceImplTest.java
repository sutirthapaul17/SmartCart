package com.example.SmartCart.User.Service.Impl;

import com.example.SmartCart.User.Dto.Auth.LoginResponse;
import com.example.SmartCart.User.Dto.User.CreateUserDto;
import com.example.SmartCart.User.Dto.Address.AddressRequestDto;
import com.example.SmartCart.User.Entity.User;
import com.example.SmartCart.User.Security.AuthUtil;
import com.example.SmartCart.User.Service.UserService;
import com.example.SmartCart.User.enums.Gender;
import com.example.SmartCart.common.Exception.ConflictException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private AuthUtil jwtService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthServiceImpl authService;

    private CreateUserDto createUserDto;
    private User user;

    @BeforeEach
    void setUp() {

        AddressRequestDto address = new AddressRequestDto(
                "Home",
                "123 Main Street",
                "Near Park",
                "Kolkata",
                "West Bengal",
                "India",
                "700001",
                true
        );

        createUserDto = new CreateUserDto(
                "Test",
                "Customer",
                "customer1@test.com",
                "9876543210",
                "password123",
                LocalDate.of(2002, 5, 15),
                Gender.MALE,
                List.of(address)
        );

        user = new User();
        user.setFirstName("Test");
        user.setLastName("Customer");
        user.setEmail("customer1@test.com");
    }

    @Test
    void shouldRegisterCustomerAndReturnJwtToken() {

        // Arrange

        when(authenticationManager.authenticate(
                any(UsernamePasswordAuthenticationToken.class)
        )).thenAnswer(invocation -> {

            Authentication request = invocation.getArgument(0);

            return new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    List.of()
            );
        });

        when(jwtService.generateToken(user))
                .thenReturn("mock-jwt-token");

        // Act

        LoginResponse response = authService.register(createUserDto);

        // Assert

        assertNotNull(response);
        assertEquals("mock-jwt-token", response.accessToken());
        assertEquals("Bearer", response.tokenType());

        // Verify registration was called

        verify(userService).createUser(createUserDto);

        // Verify authentication was performed

        ArgumentCaptor<UsernamePasswordAuthenticationToken>
                authCaptor =
                ArgumentCaptor.forClass(
                        UsernamePasswordAuthenticationToken.class
                );

        verify(authenticationManager).authenticate(authCaptor.capture());

        UsernamePasswordAuthenticationToken authRequest =
                authCaptor.getValue();

        assertEquals(
                createUserDto.email(),
                authRequest.getPrincipal()
        );

        assertEquals(
                createUserDto.password(),
                authRequest.getCredentials()
        );

        // Verify JWT generation

        verify(jwtService).generateToken(user);
    }

    @Test
    void shouldNotAuthenticateWhenRegistrationFails() {

        // Arrange

        doThrow(new ConflictException("Email already exists"))
                .when(userService)
                .createUser(createUserDto);

        // Act & Assert

        assertThrows(
                ConflictException.class,
                () -> authService.register(createUserDto)
        );

        // Registration failure must prevent login

        verify(userService).createUser(createUserDto);

        verifyNoInteractions(authenticationManager);
        verifyNoInteractions(jwtService);
    }
}

