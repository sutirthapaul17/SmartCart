package com.example.SmartCart.User.Controller;


import com.example.SmartCart.User.Dto.Seller.PendingSellerDto;
import com.example.SmartCart.User.Dto.User.UserResponseDto;
import com.example.SmartCart.User.Service.AdminService;
import com.example.SmartCart.common.Handler.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    /**
     * Get all users
     */
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getAllUsers() {

        return ResponseEntity.ok(
                ApiResponse.<List<UserResponseDto>>builder()
                        .message("Users fetched successfully.")
                        .data(adminService.getAllUsers())
                        .build()
        );
    }

    /**
     * Get user by ID
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserById(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                ApiResponse.<UserResponseDto>builder()
                        .message("User fetched successfully.")
                        .data(adminService.getUserById(userId))
                        .build()
        );
    }

    /**
     * Block a user
     */
    @PatchMapping("/users/{userId}/block")
    public ResponseEntity<ApiResponse<Void>> blockUser(
            @PathVariable Long userId) {

        adminService.blockUser(userId);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .message("User blocked successfully.")
                        .build()
        );
    }

    /**
     * Unblock a user
     */
    @PatchMapping("/users/{userId}/unblock")
    public ResponseEntity<ApiResponse<Void>> unblockUser(
            @PathVariable Long userId) {

        adminService.unblockUser(userId);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .message("User unblocked successfully.")
                        .build()
        );
    }

    /**
     * Get pending seller approvals
     */
    @GetMapping("/sellers/pending")
    public ResponseEntity<ApiResponse<List<PendingSellerDto>>> getPendingSellers() {

        return ResponseEntity.ok(
                ApiResponse.<List<PendingSellerDto>>builder()
                        .message("Pending sellers fetched successfully.")
                        .data(adminService.getPendingSellers())
                        .build()
        );
    }

    /**
     * Approve seller
     */
    @PatchMapping("/sellers/{sellerId}/approve")
    public ResponseEntity<ApiResponse<Void>> approveSeller(
            @PathVariable Long sellerId) {

        adminService.approveSeller(sellerId);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .message("Seller approved successfully.")
                        .build()
        );
    }

    @PatchMapping("/sellers/{sellerId}/reject")
    public ResponseEntity<ApiResponse<Void>> rejectSeller(
            @PathVariable Long sellerId) {

        adminService.rejectSeller(sellerId);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .message("Seller rejected successfully.")
                        .build()
        );
    }
}
