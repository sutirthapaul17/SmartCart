package com.example.SmartCart.Product.Controller;

import com.example.SmartCart.Products.Controller.SellerProductController;
import com.example.SmartCart.Products.Dto.RequestDto.CreateProductRequestDto;
import com.example.SmartCart.Products.Dto.RequestDto.UpdateProductRequest;
import com.example.SmartCart.Products.Dto.ResponseDto.ProductDeleteResponse;
import com.example.SmartCart.Products.Dto.ResponseDto.ProductResponse;
import com.example.SmartCart.Products.Dto.ResponseDto.SellerProductResponse;
import com.example.SmartCart.Products.Service.SellerProductService;
import com.example.SmartCart.Products.enums.ProductStatus;
import com.example.SmartCart.User.Entity.User;
import com.example.SmartCart.User.enums.UserRole;
import com.example.SmartCart.User.enums.UserStatus;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SellerProductController.class)
@Import(SellerProductControllerSecurityTest.MethodSecurityConfig.class)
class SellerProductControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SellerProductService sellerProductService;

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

    private String createProductRequest() {

        return """
                {
                    "name": "Test Laptop",
                    "description": "Test laptop description",
                    "categoryId": 1,
                    "price": 55000.00,
                    "stock": 10
                }
                """;
    }

    private ProductResponse productResponse() {

        return new ProductResponse(
                100L,
                "Test Laptop",
                new BigDecimal("55000.00"),
                null,
                "Electronics",
                ProductStatus.ACTIVE
        );
    }

    @Test
    void sellerShouldCreateProduct() throws Exception {

        when(sellerProductService.createProduct(
                any(CreateProductRequestDto.class)))
                .thenReturn(productResponse());

        mockMvc.perform(post("/seller/products")
                        .with(user(createUser(UserRole.SELLER)))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createProductRequest()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message")
                        .value("Product created successfully."))
                .andExpect(jsonPath("$.data.name")
                        .value("Test Laptop"));

        verify(sellerProductService)
                .createProduct(any(CreateProductRequestDto.class));
    }

    @Test
    void customerShouldNotCreateProduct() throws Exception {

        mockMvc.perform(post("/seller/products")
                        .with(user(createUser(UserRole.CUSTOMER)))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createProductRequest()))
                .andExpect(status().isForbidden());

        verifyNoInteractions(sellerProductService);
    }

    @Test
    void adminShouldNotCreateSellerProduct() throws Exception {

        mockMvc.perform(post("/seller/products")
                        .with(user(createUser(UserRole.ADMIN)))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createProductRequest()))
                .andExpect(status().isForbidden());

        verifyNoInteractions(sellerProductService);
    }

    @Test
    void sellerShouldFetchOwnProducts() throws Exception {

        when(sellerProductService.getSellerProducts(any()))
                .thenReturn(Page.empty());

        mockMvc.perform(get("/seller/products/me")
                        .with(user(createUser(UserRole.SELLER))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Products fetched successfully."));

        verify(sellerProductService).getSellerProducts(any());
    }

    @Test
    void customerShouldNotFetchSellerProducts() throws Exception {

        mockMvc.perform(get("/seller/products/me")
                        .with(user(createUser(UserRole.CUSTOMER))))
                .andExpect(status().isForbidden());

        verifyNoInteractions(sellerProductService);
    }

    @Test
    void sellerShouldDeleteProduct() throws Exception {

        when(sellerProductService.deleteProduct(100L))
                .thenReturn(new ProductDeleteResponse(
                        100L,
                        "Product deleted successfully."
                ));

        mockMvc.perform(delete("/seller/products/100")
                        .with(user(createUser(UserRole.SELLER)))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.productId")
                        .value(100));

        verify(sellerProductService).deleteProduct(100L);
    }
}
