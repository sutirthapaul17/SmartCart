package com.example.SmartCart.User.Controller;

import com.example.SmartCart.User.Dto.Auth.LoginResponse;
import com.example.SmartCart.User.Service.AuthService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;
    private LocalValidatorFactoryBean validator;

    @BeforeEach
    void setUp() {

        validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders
                .standaloneSetup(authController)
                .setValidator(validator)
                .build();
    }

    @Test
    void shouldRegisterCustomerSuccessfully() throws Exception {

        // Arrange

        LoginResponse response = new LoginResponse(
                "mock-jwt-token",
                "Bearer"
        );

        when(authService.register(any()))
                .thenReturn(response);

        String requestBody = """
                {
                    "firstName": "Test",
                    "lastName": "Customer",
                    "email": "customer1@test.com",
                    "phoneNumber": "9876543210",
                    "password": "password123",
                    "dateOfBirth": "2002-05-15",
                    "gender": "MALE",
                    "addresses": [
                        {
                            "addressLabel": "Home",
                            "addressLine1": "123 Main Street",
                            "addressLine2": "Near Park",
                            "city": "Kolkata",
                            "state": "West Bengal",
                            "country": "India",
                            "postalCode": "700001",
                            "defaultAddress": true
                        }
                    ]
                }
                """;

        // Act & Assert

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_JSON
                ))
                .andExpect(jsonPath("$.message")
                        .value("Registration successful"))
                .andExpect(jsonPath("$.data.accessToken")
                        .value("mock-jwt-token"))
                .andExpect(jsonPath("$.data.tokenType")
                        .value("Bearer"));

        verify(authService, times(1))
                .register(any());
    }

    @Test
    void shouldRejectRegistrationWithInvalidEmail() throws Exception {

        String requestBody = """
                {
                    "firstName": "Test",
                    "lastName": "Customer",
                    "email": "invalid-email",
                    "phoneNumber": "9876543210",
                    "password": "password123",
                    "dateOfBirth": "2002-05-15",
                    "gender": "MALE",
                    "addresses": [
                        {
                            "addressLine1": "123 Main Street",
                            "city": "Kolkata",
                            "state": "West Bengal",
                            "country": "India",
                            "postalCode": "700001",
                            "defaultAddress": true
                        }
                    ]
                }
                """;

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authService);
    }

    @Test
    void shouldRejectRegistrationWithMissingRequiredFields()
            throws Exception {

        String requestBody = """
                {
                    "firstName": "",
                    "lastName": "",
                    "email": "",
                    "phoneNumber": "",
                    "password": "",
                    "dateOfBirth": null,
                    "gender": null,
                    "addresses": []
                }
                """;

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authService);
    }

    @Test
    void shouldRejectRegistrationWithInvalidPhoneNumber()
            throws Exception {

        String requestBody = """
                {
                    "firstName": "Test",
                    "lastName": "Customer",
                    "email": "customer@test.com",
                    "phoneNumber": "12345",
                    "password": "password123",
                    "dateOfBirth": "2002-05-15",
                    "gender": "MALE",
                    "addresses": [
                        {
                            "addressLine1": "123 Main Street",
                            "city": "Kolkata",
                            "state": "West Bengal",
                            "country": "India",
                            "postalCode": "700001",
                            "defaultAddress": true
                        }
                    ]
                }
                """;

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authService);
    }
}

