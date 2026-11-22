package com.example.SmartCart.User.Service.Impl;

import com.example.SmartCart.Mapper.User.AddressMapper;
import com.example.SmartCart.User.Dto.Address.AddressRequestDto;
import com.example.SmartCart.User.Dto.Address.AddressResponseDto;
import com.example.SmartCart.User.Entity.Address;
import com.example.SmartCart.User.Entity.User;
import com.example.SmartCart.User.Repository.AddressRepo;
import com.example.SmartCart.User.Repository.UserRepo;
import com.example.SmartCart.common.Exception.ResourceNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

    @Mock
    private AddressRepo addressRepo;

    @Mock
    private AddressMapper addressMapper;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private AddressServiceImpl addressService;

    private User user;
    private Address address;
    private AddressRequestDto request;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(3L);
        user.setFirstName("Test");
        user.setLastName("Customer");

        request = new AddressRequestDto(
                "Home",
                "123 Main Street",
                "Near Park",
                "Kolkata",
                "West Bengal",
                "India",
                "700001",
                true
        );

        address = new Address();
        address.setId(20L);
        address.setUser(user);
        address.setAddressLabel("Home");
        address.setAddressLine1("123 Main Street");
        address.setCity("Kolkata");
        address.setState("West Bengal");
        address.setCountry("India");
        address.setPostalCode("700001");
        address.setDefaultAddress(true);
    }

    @Test
    void shouldAddAddressSuccessfully() {

        when(userRepo.findById(3L))
                .thenReturn(Optional.of(user));

        when(addressMapper.toEntity(request))
                .thenReturn(address);

        when(addressRepo.findByUserId(3L))
                .thenReturn(List.of());

        when(addressRepo.save(any(Address.class)))
                .thenReturn(address);

        AddressResponseDto expected =
                new AddressResponseDto(
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

        when(addressMapper.toResponseDto(address))
                .thenReturn(expected);

        AddressResponseDto actual =
                addressService.addAddress(3L, request);

        assertNotNull(actual);
        assertEquals(20L, actual.id());
        assertEquals("Kolkata", actual.city());
        assertTrue(actual.defaultAddress());

        assertEquals(user, address.getUser());

        verify(addressRepo).save(address);
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {

        when(userRepo.findById(3L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> addressService.addAddress(3L, request)
        );

        verify(addressRepo, never()).save(any());
    }

    @Test
    void shouldClearPreviousDefaultAddressWhenAddingNewDefault()
    {

        Address previousAddress = new Address();
        previousAddress.setDefaultAddress(true);

        when(userRepo.findById(3L))
                .thenReturn(Optional.of(user));

        when(addressMapper.toEntity(request))
                .thenReturn(address);

        when(addressRepo.findByUserId(3L))
                .thenReturn(List.of(previousAddress));

        when(addressRepo.save(any(Address.class)))
                .thenReturn(address);

        when(addressMapper.toResponseDto(address))
                .thenReturn(new AddressResponseDto(
                        20L, "Home", null, null,
                        "123 Main Street", null,
                        "Kolkata", "West Bengal", "India",
                        "700001", true
                ));

        addressService.addAddress(3L, request);

        assertFalse(previousAddress.getDefaultAddress());
        assertTrue(address.getDefaultAddress());

        verify(addressRepo).saveAll(List.of(previousAddress));
    }

    @Test
    void shouldRejectUpdatingAddressOwnedByAnotherUser() {

        User anotherUser = new User();
        anotherUser.setId(4L);

        address.setUser(anotherUser);

        when(addressRepo.findById(20L))
                .thenReturn(Optional.of(address));

        assertThrows(
                IllegalArgumentException.class,
                () -> addressService.updateAddress(3L, 20L, request)
        );

        verify(addressRepo, never()).save(any());
    }

    @Test
    void shouldDeleteAddressSuccessfully() {

        when(addressRepo.findById(20L))
                .thenReturn(Optional.of(address));

        addressService.deleteAddress(3L, 20L);

        verify(addressRepo).delete(address);
    }

    @Test
    void shouldRejectDeletingAnotherUsersAddress() {

        User anotherUser = new User();
        anotherUser.setId(4L);

        address.setUser(anotherUser);

        when(addressRepo.findById(20L))
                .thenReturn(Optional.of(address));

        assertThrows(
                IllegalArgumentException.class,
                () -> addressService.deleteAddress(3L, 20L)
        );

        verify(addressRepo, never()).delete(any());
    }
}
