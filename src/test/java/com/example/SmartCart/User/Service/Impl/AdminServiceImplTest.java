package com.example.SmartCart.User.Service.Impl;

import com.example.SmartCart.Mapper.User.SellerMapper;
import com.example.SmartCart.Mapper.User.UserMapper;
import com.example.SmartCart.User.Entity.SellerProfile;
import com.example.SmartCart.User.Entity.User;
import com.example.SmartCart.User.Repository.SellerProfileRepository;
import com.example.SmartCart.User.Repository.UserRepo;
import com.example.SmartCart.User.enums.SellerStatus;
import com.example.SmartCart.User.enums.UserRole;
import com.example.SmartCart.User.enums.UserStatus;
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
class AdminServiceImplTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private UserMapper userMapper;

    @Mock
    private SellerProfileRepository sellerProfileRepo;

    @Mock
    private SellerMapper sellerMapper;

    @InjectMocks
    private AdminServiceImpl adminService;

    private User user;
    private SellerProfile seller;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(2L);
        user.setFirstName("Test");
        user.setLastName("Seller");
        user.getRoles().add(UserRole.CUSTOMER);
        user.setStatus(UserStatus.ACTIVE);

        seller = new SellerProfile();
        seller.setUser(user);
        seller.setStoreName("Test Store");
        seller.setSellerStatus(SellerStatus.PENDING);
    }

    @Test
    void shouldApproveSellerAndAssignSellerRole() {

        when(sellerProfileRepo.findById(10L))
                .thenReturn(Optional.of(seller));

        adminService.approveSeller(10L);

        assertEquals(SellerStatus.APPROVED, seller.getSellerStatus());

        assertTrue(user.getRoles().contains(UserRole.CUSTOMER));
        assertTrue(user.getRoles().contains(UserRole.SELLER));

        verify(sellerProfileRepo).findById(10L);
    }

    @Test
    void shouldNotApproveAlreadyApprovedSeller() {

        seller.setSellerStatus(SellerStatus.APPROVED);

        when(sellerProfileRepo.findById(10L))
                .thenReturn(Optional.of(seller));

        assertThrows(
                IllegalStateException.class,
                () -> adminService.approveSeller(10L)
        );

        assertFalse(user.getRoles().contains(UserRole.SELLER));
    }

    @Test
    void shouldThrowExceptionWhenSellerDoesNotExist() {

        when(sellerProfileRepo.findById(10L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> adminService.approveSeller(10L)
        );
    }

    @Test
    void shouldRejectPendingSeller() {

        when(sellerProfileRepo.findById(10L))
                .thenReturn(Optional.of(seller));

        adminService.rejectSeller(10L);

        assertEquals(SellerStatus.REJECTED, seller.getSellerStatus());

        assertFalse(user.getRoles().contains(UserRole.SELLER));
    }

    @Test
    void shouldNotRejectAlreadyApprovedSeller() {

        seller.setSellerStatus(SellerStatus.APPROVED);

        when(sellerProfileRepo.findById(10L))
                .thenReturn(Optional.of(seller));

        assertThrows(
                IllegalStateException.class,
                () -> adminService.rejectSeller(10L)
        );
    }

    @Test
    void shouldBlockUserSuccessfully() {

        when(userRepo.findById(2L))
                .thenReturn(Optional.of(user));

        adminService.blockUser(2L);

        assertEquals(UserStatus.BLOCKED, user.getStatus());
    }

    @Test
    void shouldUnblockBlockedUser() {

        user.setStatus(UserStatus.BLOCKED);

        when(userRepo.findById(2L))
                .thenReturn(Optional.of(user));

        adminService.unblockUser(2L);

        assertEquals(UserStatus.ACTIVE, user.getStatus());
    }

    @Test
    void shouldNotUnblockActiveUser() {

        when(userRepo.findById(2L))
                .thenReturn(Optional.of(user));

        assertThrows(
                IllegalStateException.class,
                () -> adminService.unblockUser(2L)
        );
    }
}

