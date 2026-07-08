package com.example.SmartCart.User.Service;

import com.example.SmartCart.User.Dto.Address.AddressRequestDto;
import com.example.SmartCart.User.Dto.Address.AddressResponseDto;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface AddressService {
    List<AddressResponseDto> getAddresses();

    AddressResponseDto addAddress(@Valid AddressRequestDto request);

    @Nullable AddressResponseDto updateAddress(Long addressId, @Valid AddressRequestDto request);

    void deleteAddress(Long addressId);

}
