
package com.example.SmartCart.User.Controller;

import com.example.SmartCart.User.Dto.Address.AddressRequestDto;
import com.example.SmartCart.User.Dto.Address.AddressResponseDto;
import com.example.SmartCart.User.Entity.User;
import com.example.SmartCart.User.Service.AddressService;
import com.example.SmartCart.User.enums.UserRole;
import com.example.SmartCart.User.enums.UserStatus;

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
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AddressController.class)
@Import(AddressControllerSecurityTest.MethodSecurityConfig.class)
class AddressControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AddressService addressService;

    @TestConfiguration
    @EnableMethodSecurity
    static class MethodSecurityConfig {
    }

    private UserDetails createUser(UserRole role) {

        User user = new User();
        user.setId(3L);
        user.setFirstName("Test");
        user.setLastName("Customer");
        user.setEmail("customer@gmail.com");
        user.setPassword("encodedPassword");
        user.setPhoneNumber("9876543210");
        user.setRoles(new HashSet<>());
        user.getRoles().add(role);
        user.setStatus(UserStatus.ACTIVE);

        return user;
    }

    private String addressRequest() {

        return """
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
                """;
    }

    private AddressResponseDto addressResponse() {

        return new AddressResponseDto(
                20L,
                "Home",
                "Test Customer",
                "9876543210",
                "123 Main Street",
                "Near Park",
                "Kolkata",
                "West Bengal",
                "India",
                "700001",
                true
        );
    }

    @Test
    void customerShouldAccessOwnAddresses() throws Exception {

        when(addressService.getAddresses(3L))
                .thenReturn(List.of(addressResponse()));

        mockMvc.perform(get("/users/me/address")
                        .with(user(createUser(UserRole.CUSTOMER))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].city")
                        .value("Kolkata"));

        verify(addressService).getAddresses(3L);
    }

    @Test
    void sellerShouldAccessOwnAddresses() throws Exception {

        when(addressService.getAddresses(3L))
                .thenReturn(List.of());

        mockMvc.perform(get("/users/me/address")
                        .with(user(createUser(UserRole.SELLER))))
                .andExpect(status().isOk());

        verify(addressService).getAddresses(3L);
    }

    @Test
    void adminShouldAccessAddresses() throws Exception {

        when(addressService.getAddresses(3L))
                .thenReturn(List.of());

        mockMvc.perform(get("/users/me/address")
                        .with(user(createUser(UserRole.ADMIN))))
                .andExpect(status().isOk());

        verify(addressService).getAddresses(3L);
    }

    @Test
    void customerShouldAddAddressSuccessfully() throws Exception {

        when(addressService.addAddress(
                eq(3L), any(AddressRequestDto.class)))
                .thenReturn(addressResponse());

        mockMvc.perform(post("/users/me/address")
                        .with(user(createUser(UserRole.CUSTOMER)))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressRequest()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message")
                        .value("Address added successfully."))
                .andExpect(jsonPath("$.data.city")
                        .value("Kolkata"));

        verify(addressService)
                .addAddress(eq(3L), any(AddressRequestDto.class));
    }

    @Test
    void customerShouldDeleteOwnAddress() throws Exception {

        mockMvc.perform(delete("/users/me/address/20")
                        .with(user(createUser(UserRole.CUSTOMER)))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Address deleted successfully."));

        verify(addressService).deleteAddress(3L, 20L);
    }

    @Test
    void unauthenticatedUserShouldNotAccessAddresses()
            throws Exception {

        mockMvc.perform(get("/users/me/address"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(addressService);
    }
}
