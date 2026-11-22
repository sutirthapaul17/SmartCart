package com.example.SmartCart.User.Service.Impl;

import com.example.SmartCart.Mapper.User.SellerMapper;
import com.example.SmartCart.User.Dto.Seller.SellerProfileRequestDto;
import com.example.SmartCart.User.Dto.Seller.SellerProfileResponseDto;
import com.example.SmartCart.User.Entity.SellerProfile;
import com.example.SmartCart.User.Entity.User;
import com.example.SmartCart.User.Repository.SellerProfileRepository;
import com.example.SmartCart.User.Repository.UserRepo;
import com.example.SmartCart.User.enums.SellerStatus;
import com.example.SmartCart.common.Exception.BadRequestException;
import com.example.SmartCart.common.Exception.ResourceNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SellerProfileServiceImplTest {

    @Mock
    private SellerProfileRepository sellerProfileRepository;

    @Mock
    private UserRepo userRepo;

    @Mock
    private SellerMapper sellerMapper;

    @InjectMocks
    private SellerProfileServiceImpl sellerService;

    private User user;
    private SellerProfileRequestDto request;
    private SellerProfile seller;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(2L);
        user.setFirstName("Test");
        user.setLastName("Seller");

        request = new SellerProfileRequestDto(
                "Test Store",
                "Electronics store",
                "GST123456",
                "ABCDE1234F"
        );

        seller = new SellerProfile();
        seller.setStoreName("Test Store");
        seller.setStoreDescription("Electronics store");
        seller.setGstNumber("GST123456");
        seller.setPanNumber("ABCDE1234F");
    }

    @Test
    void shouldRegisterSellerSuccessfully() {

        when(userRepo.findById(2L))
                .thenReturn(Optional.of(user));

        when(sellerProfileRepository.existsByUserId(2L))
                .thenReturn(false);

        when(sellerMapper.toEntity(request))
                .thenReturn(seller);

        when(sellerProfileRepository.save(seller))
                .thenReturn(seller);

        SellerProfileResponseDto expected =
                new SellerProfileResponseDto(
                        10L,
                        "Test Store",
                        "Electronics store",
                        "GST123456",
                        SellerStatus.PENDING
                );

        when(sellerMapper.toResponseDto(seller))
                .thenReturn(expected);

        SellerProfileResponseDto actual =
                sellerService.registerAsSeller(2L, request);

        assertNotNull(actual);
        assertEquals(expected, actual);

        assertEquals(user, seller.getUser());
        assertEquals(SellerStatus.PENDING, seller.getSellerStatus());

        verify(userRepo).findById(2L);
        verify(sellerProfileRepository).existsByUserId(2L);
        verify(sellerProfileRepository).save(seller);
    }

    @Test
    void shouldRejectDuplicateSellerApplication() {

        when(userRepo.findById(2L))
                .thenReturn(Optional.of(user));

        when(sellerProfileRepository.existsByUserId(2L))
                .thenReturn(true);

        assertThrows(
                BadRequestException.class,
                () -> sellerService.registerAsSeller(2L, request)
        );

        verify(sellerProfileRepository, never()).save(any());
        verify(sellerMapper, never()).toEntity(any());
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {

        when(userRepo.findById(2L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> sellerService.registerAsSeller(2L, request)
        );

        verify(sellerProfileRepository, never()).save(any());
    }

    @Test
    void shouldFetchSellerProfileSuccessfully() {

        when(sellerProfileRepository.findByUserId(2L))
                .thenReturn(Optional.of(seller));

        when(sellerMapper.toResponseDto(seller))
                .thenReturn(new SellerProfileResponseDto(
                        10L,
                        "Test Store",
                        "Electronics store",
                        "GST123456",
                        SellerStatus.PENDING
                ));

        SellerProfileResponseDto response =
                sellerService.getSellerProfile(2L);

        assertEquals("Test Store", response.storeName());
        assertEquals(SellerStatus.PENDING, response.status());
    }

    @Test
    void shouldThrowExceptionWhenSellerProfileDoesNotExist() {

        when(sellerProfileRepository.findByUserId(2L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> sellerService.getSellerProfile(2L)
        );
    }
}
