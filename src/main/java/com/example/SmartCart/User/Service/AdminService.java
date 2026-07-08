package com.example.SmartCart.User.Service;

import com.example.SmartCart.User.Dto.User.PendingSellerDto;
import com.example.SmartCart.User.Dto.User.UserResponseDto;

import java.util.List;

public interface AdminService {

    List<UserResponseDto> getAllUsers();

    UserResponseDto getUserById(Long userId);

    void blockUser(Long userId);

    void unblockUser(Long userId);

    List<PendingSellerDto> getPendingSellers();

    void approveSeller(Long sellerId);
}
