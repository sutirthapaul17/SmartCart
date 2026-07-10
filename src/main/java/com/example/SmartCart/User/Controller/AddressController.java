package com.example.SmartCart.User.Controller;


import com.example.SmartCart.User.Dto.Address.AddressRequestDto;
import com.example.SmartCart.User.Dto.Address.AddressResponseDto;
import com.example.SmartCart.User.Service.AddressService;
import com.example.SmartCart.common.Handler.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/me/address")
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AddressResponseDto>>> getAddresses() {
        Long userId = 1L;

        List<AddressResponseDto> addresses = addressService.getAddresses(userId);

        return ResponseEntity.ok(
                new ApiResponse<>("Addresses fetched successfully.", addresses)
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AddressResponseDto>> addAddress(
            @Valid @RequestBody AddressRequestDto request) {
        Long userId = 1L;
        AddressResponseDto response = addressService.addAddress(userId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Address added successfully.", response));
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<ApiResponse<AddressResponseDto>> updateAddress(
            @PathVariable Long addressId,
            @Valid @RequestBody AddressRequestDto request) {
        Long userId = 1L;
        AddressResponseDto response = addressService.updateAddress(userId, addressId, request);

        return ResponseEntity.ok(
                new ApiResponse<>("Address updated successfully.", response)
        );
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<ApiResponse<String>> deleteAddress(
            @PathVariable Long addressId) {
        Long userId = 1L;

        addressService.deleteAddress(userId,addressId);

        return ResponseEntity.ok(
                new ApiResponse<>("Address deleted successfully.", null)
        );
    }
}
