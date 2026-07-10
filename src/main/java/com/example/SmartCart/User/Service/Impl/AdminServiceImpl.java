package com.example.SmartCart.User.Service.Impl;

import com.example.SmartCart.Mapper.User.SellerMapper;
import com.example.SmartCart.Mapper.User.UserMapper;
import com.example.SmartCart.User.Dto.Seller.PendingSellerDto;
import com.example.SmartCart.User.Dto.User.UserResponseDto;
import com.example.SmartCart.User.Entity.SellerProfile;
import com.example.SmartCart.User.Entity.User;
import com.example.SmartCart.User.Repository.SellerProfileRepository;
import com.example.SmartCart.User.Repository.UserRepo;
import com.example.SmartCart.User.Service.AdminService;
import com.example.SmartCart.User.enums.SellerStatus;
import com.example.SmartCart.User.enums.UserStatus;
import com.example.SmartCart.common.Exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepo userRepo;;
    private final UserMapper userMapper;
    private final SellerProfileRepository sellerProfileRepo;
    private final SellerMapper sellerMapper;


    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepo.findAll()
                .stream()
                .map(userMapper::toUserResponseDto)
                .toList();
    }

    @Override
    public UserResponseDto getUserById(Long userId) {
        User user = getUserByIdOrThrow(userId);
        return userMapper.toUserResponseDto(user);
    }

    @Override
    @Transactional
    public void blockUser(Long userId) {
        User user = getUserByIdOrThrow(userId);
        user.setStatus(UserStatus.BLOCKED);
    }

    @Override
    public void unblockUser(Long userId) {
        User user = getUserByIdOrThrow(userId);
        if(user.getStatus() == UserStatus.BLOCKED) {
            user.setStatus(UserStatus.ACTIVE);
        }else{
            throw new IllegalStateException("User is not blocked and cannot be unblocked.");
        }
    }

    @Override
    public List<PendingSellerDto> getPendingSellers() {
        return sellerProfileRepo.findByStatus(SellerStatus.PENDING)
                .stream()
                .map(sellerMapper::toPendingSellerDto)
                .toList();
    }


    @Override
    @Transactional
    public void approveSeller(Long sellerId) {

        SellerProfile seller = getSellerByIdOrThrow(sellerId);

        if (seller.getSellerStatus() != SellerStatus.PENDING) {
            throw new IllegalStateException("Seller is not pending approval.");
        }

        seller.setSellerStatus(SellerStatus.APPROVED);
    }

    @Override
    @Transactional
    public void rejectSeller(Long sellerId) {

        SellerProfile seller = getSellerByIdOrThrow(sellerId);

        if (seller.getSellerStatus() != SellerStatus.PENDING) {
            throw new IllegalStateException("Seller is not pending approval.");
        }

        seller.setSellerStatus(SellerStatus.REJECTED);
    }


    //=========================Helper functions====================
    private User getUserByIdOrThrow(Long userId) {
        return userRepo.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + userId));
    }

    private SellerProfile getSellerByIdOrThrow(Long sellerId) {
        return sellerProfileRepo.findById(sellerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Seller not found with id: " + sellerId));
    }



}
