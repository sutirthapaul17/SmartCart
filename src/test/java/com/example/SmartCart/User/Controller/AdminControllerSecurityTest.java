
package com.example.SmartCart.User.Controller;

import com.example.SmartCart.User.Entity.User;
import com.example.SmartCart.User.Security.JwtService;
import com.example.SmartCart.User.Service.AdminService;
import com.example.SmartCart.User.enums.UserRole;
import com.example.SmartCart.User.enums.UserStatus;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
@Import(AdminControllerSecurityTest.MethodSecurityConfig.class)
class AdminControllerSecurityTest {

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private AdminService adminService;

    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    @EnableMethodSecurity
    static class MethodSecurityConfig {
    }

    private UserDetails createUser(UserRole role) {

        User user = new User();
        user.setId(1L);
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("test@gmail.com");
        user.setPassword("encodedPassword");
        user.setPhoneNumber("9876543210");

        user.setRoles(new HashSet<>());
        user.getRoles().add(role);

        user.setStatus(UserStatus.ACTIVE);

        return user;
    }

    @Test
    void adminShouldAccessAllUsers() throws Exception {

        when(adminService.getAllUsers()).thenReturn(java.util.List.of());

        mockMvc.perform(get("/admin/users")
                        .with(user(createUser(UserRole.ADMIN))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Users fetched successfully."));

        verify(adminService).getAllUsers();
    }

    @Test
    void customerShouldNotAccessAllUsers() throws Exception {

        mockMvc.perform(get("/admin/users")
                        .with(user(createUser(UserRole.CUSTOMER))))
                .andExpect(status().isForbidden());

        verifyNoInteractions(adminService);
    }

    @Test
    void sellerShouldNotAccessAllUsers() throws Exception {

        mockMvc.perform(get("/admin/users")
                        .with(user(createUser(UserRole.SELLER))))
                .andExpect(status().isForbidden());

        verifyNoInteractions(adminService);
    }

    @Test
    void adminShouldApproveSeller() throws Exception {

        mockMvc.perform(patch("/admin/sellers/10/approve")
                        .with(user(createUser(UserRole.ADMIN))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Seller approved successfully."));

        verify(adminService).approveSeller(10L);
    }

    @Test
    void customerShouldNotApproveSeller() throws Exception {

        mockMvc.perform(patch("/admin/sellers/10/approve")
                        .with(user(createUser(UserRole.CUSTOMER))))
                .andExpect(status().isForbidden());

        verifyNoInteractions(adminService);
    }

    @Test
    void sellerShouldNotBlockUsers() throws Exception {

        mockMvc.perform(patch("/admin/users/5/block")
                        .with(user(createUser(UserRole.SELLER))))
                .andExpect(status().isForbidden());

        verifyNoInteractions(adminService);
    }
}
