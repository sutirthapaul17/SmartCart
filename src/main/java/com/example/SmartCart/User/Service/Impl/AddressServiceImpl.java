package com.example.SmartCart.User.Service.Impl;

import com.example.SmartCart.User.Dto.Address.AddressRequestDto;
import com.example.SmartCart.User.Dto.Address.AddressResponseDto;
import com.example.SmartCart.User.Service.AddressService;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class AddressServiceImpl implements AddressService {
    @Override
    public List<AddressResponseDto> getAddresses() {
        return List.of();
    }

    @Override
    public AddressResponseDto addAddress(AddressRequestDto request) {
        return null;
    }

    @Override
    public @Nullable AddressResponseDto updateAddress(Long addressId, AddressRequestDto request) {
        return null;
    }

    @Override
    public void deleteAddress(Long addressId) {

    }
}
