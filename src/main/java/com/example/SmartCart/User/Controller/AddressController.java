package com.example.SmartCart.User.Controller;


import com.example.SmartCart.User.Dto.Address.AddressRequestDto;
import com.example.SmartCart.User.Dto.Address.AddressResponseDto;
import com.example.SmartCart.User.Entity.User;
import com.example.SmartCart.User.Service.AddressService;
import com.example.SmartCart.common.Handler.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/me/address")
@PreAuthorize("hasAnyRole('CUSTOMER','SELLER','ADMIN')")
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AddressResponseDto>>> getAddresses() {
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Long userId = user.getId();

        List<AddressResponseDto> addresses = addressService.getAddresses(userId);

        return ResponseEntity.ok(
                new ApiResponse<>("Addresses fetched successfully.", addresses)
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AddressResponseDto>> addAddress(
            @Valid @RequestBody AddressRequestDto request) {
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Long userId = null;
        if (user != null) {
            userId = user.getId();
        }
        AddressResponseDto response = addressService.addAddress(userId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Address added successfully.", response));
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<ApiResponse<AddressResponseDto>> updateAddress(
            @PathVariable Long addressId,
            @Valid @RequestBody AddressRequestDto request) {
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Long userId = null;
        if (user != null) {
            userId = user.getId();
        }
        AddressResponseDto response = addressService.updateAddress(userId, addressId, request);

        return ResponseEntity.ok(
                new ApiResponse<>("Address updated successfully.", response)
        );
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<ApiResponse<String>> deleteAddress(
            @PathVariable Long addressId) {
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Long userId = null;
        if (user != null) {
            userId = user.getId();
        }

        addressService.deleteAddress(userId, addressId);

        return ResponseEntity.ok(
                new ApiResponse<>("Address deleted successfully.", null)
        );
    }
}
