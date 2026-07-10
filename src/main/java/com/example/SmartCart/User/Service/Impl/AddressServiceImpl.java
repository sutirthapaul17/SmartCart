package com.example.SmartCart.User.Service.Impl;

import com.example.SmartCart.Mapper.User.AddressMapper;
import com.example.SmartCart.User.Dto.Address.AddressRequestDto;
import com.example.SmartCart.User.Dto.Address.AddressResponseDto;
import com.example.SmartCart.User.Entity.Address;
import com.example.SmartCart.User.Entity.User;
import com.example.SmartCart.User.Repository.AddressRepo;
import com.example.SmartCart.User.Repository.UserRepo;
import com.example.SmartCart.User.Service.AddressService;
import com.example.SmartCart.common.Exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepo addressRepo;
    private final AddressMapper addressMapper;
    private final UserRepo userRepo;


    @Override
    public List<AddressResponseDto> getAddresses(long userId) {
        return addressRepo.findByUserId(userId)
                .stream()
                .map(addressMapper::toResponseDto)
                .toList();
    }

    @Override
    public AddressResponseDto addAddress(Long userId, AddressRequestDto request) {

        User user = userRepo.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + userId));

        Address address = addressMapper.toEntity(request);
        address.setUser(user);

        if (Boolean.TRUE.equals(request.defaultAddress())) {
            clearDefaultAddress(userId);
            address.setDefaultAddress(true);
        } else {
            address.setDefaultAddress(false);
        }

        Address saved = addressRepo.save(address);

        return addressMapper.toResponseDto(saved);
    }


    @Override
    public @Nullable AddressResponseDto updateAddress(
            Long userId,
            Long addressId,
            AddressRequestDto request) {

        Address address = addressRepo.findById(addressId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Address not found with id: " + addressId));

        if (!address.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Address does not belong to the user.");
        }

        addressMapper.updateEntityFromDto(request, address);

        if (Boolean.TRUE.equals(request.defaultAddress())) {
            clearDefaultAddress(userId);
            address.setDefaultAddress(true);
        }

        Address updated = addressRepo.save(address);

        return addressMapper.toResponseDto(updated);
    }

    @Override
    public void deleteAddress(Long userId, Long addressId) {
        Address address = addressRepo.findById(addressId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Address not found with id: " + addressId));

        if (!address.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Address does not belong to the user.");
        }

        addressRepo.delete(address);

    }


    //============Helper methods===================
    private void clearDefaultAddress(Long userId) {

        List<Address> addresses = addressRepo.findByUserId(userId);

        addresses.forEach(address -> address.setDefaultAddress(false));

        addressRepo.saveAll(addresses);
    }
}
