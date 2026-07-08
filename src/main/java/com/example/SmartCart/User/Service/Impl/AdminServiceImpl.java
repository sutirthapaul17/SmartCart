package com.example.SmartCart.User.Service.Impl;

import com.example.SmartCart.User.Dto.User.PendingSellerDto;
import com.example.SmartCart.User.Dto.User.UserResponseDto;
import com.example.SmartCart.User.Service.AdminService;

import java.util.List;

public class AdminServiceImpl implements AdminService {
    @Override
    public List<UserResponseDto> getAllUsers() {
        return List.of();
    }

    @Override
    public UserResponseDto getUserById(Long userId) {
        return null;
    }

    @Override
    public void blockUser(Long userId) {

    }

    @Override
    public void unblockUser(Long userId) {

    }

    @Override
    public List<PendingSellerDto> getPendingSellers() {
        return List.of();
    }

    @Override
    public void approveSeller(Long sellerId) {

    }
}
