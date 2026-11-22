package com.example.SmartCart.User.Controller;

import com.example.SmartCart.User.Dto.Seller.SellerProfileRequestDto;
import com.example.SmartCart.User.Dto.Seller.SellerProfileResponseDto;
import com.example.SmartCart.User.Entity.User;
import com.example.SmartCart.User.Service.SellerProfileService;
import com.example.SmartCart.User.enums.SellerStatus;
import com.example.SmartCart.User.enums.UserRole;
import com.example.SmartCart.User.enums.UserStatus;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SellerController.class)
@Import(SellerControllerSecurityTest.MethodSecurityConfig.class)
class SellerControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SellerProfileService sellerProfileService;

    @TestConfiguration
    @EnableMethodSecurity
    static class MethodSecurityConfig {
    }

    private UserDetails createUser(UserRole role) {

        User user = new User();
        user.setId(2L);
        user.setFirstName("Test");
        user.setLastName("Seller");
        user.setEmail("seller@gmail.com");
        user.setPassword("encodedPassword");
        user.setPhoneNumber("9876543210");
        user.setRoles(new HashSet<>());
        user.getRoles().add(role);
        user.setStatus(UserStatus.ACTIVE);

        return user;
    }

    private SellerProfileResponseDto response() {

        return new SellerProfileResponseDto(
                10L,
                "Test Store",
                "Electronics Store",
                "GST123456",
                SellerStatus.PENDING
        );
    }

    private String requestBody() {

        return """
                {
                    "storeName": "Test Store",
                    "description": "Electronics Store",
                    "gstNumber": "GST123456",
                    "panNumber": "ABCDE1234F"
                }
                """;
    }

    @Test
    void customerShouldBeAbleToApplyForSeller() throws Exception {

        when(sellerProfileService.registerAsSeller(
                eq(2L), any(SellerProfileRequestDto.class)))
                .thenReturn(response());

        mockMvc.perform(post("/seller/profile/register")
                        .with(user(createUser(UserRole.CUSTOMER)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Seller registration submitted successfully."))
                .andExpect(jsonPath("$.data.storeName")
                        .value("Test Store"));

        verify(sellerProfileService)
                .registerAsSeller(eq(2L), any(SellerProfileRequestDto.class));
    }

    @Test
    void sellerShouldNotSubmitAnotherSellerApplication() throws Exception {

        mockMvc.perform(post("/seller/profile/register")
                        .with(user(createUser(UserRole.SELLER)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody()))
                .andExpect(status().isForbidden());

        verifyNoInteractions(sellerProfileService);
    }

    @Test
    void sellerShouldAccessOwnProfile() throws Exception {

        when(sellerProfileService.getSellerProfile(2L))
                .thenReturn(response());

        mockMvc.perform(get("/seller/profile/profile")
                        .with(user(createUser(UserRole.SELLER))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.storeName")
                        .value("Test Store"));

        verify(sellerProfileService).getSellerProfile(2L);
    }

    @Test
    void customerShouldNotAccessSellerProfile() throws Exception {

        mockMvc.perform(get("/seller/profile/profile")
                        .with(user(createUser(UserRole.CUSTOMER))))
                .andExpect(status().isForbidden());

        verifyNoInteractions(sellerProfileService);
    }

    @Test
    void sellerShouldUpdateOwnProfile() throws Exception {

        when(sellerProfileService.updateSellerProfile(
                eq(2L), any(SellerProfileRequestDto.class)))
                .thenReturn(response());

        mockMvc.perform(put("/seller/profile")
                        .with(user(createUser(UserRole.SELLER)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Seller profile updated successfully."));

        verify(sellerProfileService)
                .updateSellerProfile(eq(2L), any(SellerProfileRequestDto.class));
    }

    @Test
    void customerShouldNotUpdateSellerProfile() throws Exception {

        mockMvc.perform(put("/seller/profile")
                        .with(user(createUser(UserRole.CUSTOMER)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody()))
                .andExpect(status().isForbidden());

        verifyNoInteractions(sellerProfileService);
    }

    @Test
    void adminShouldNotAccessSellerProfile() throws Exception {

        mockMvc.perform(get("/seller/profile/profile")
                        .with(user(createUser(UserRole.ADMIN))))
                .andExpect(status().isForbidden());

        verifyNoInteractions(sellerProfileService);
    }
}
