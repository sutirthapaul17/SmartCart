package com.example.SmartCart.User.Controller;


import com.example.SmartCart.User.Dto.User.PendingSellerDto;
import com.example.SmartCart.User.Dto.User.UserResponseDto;
import com.example.SmartCart.User.Service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    /**
     * Get all users
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    /**
     * Get user by ID
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(
            @PathVariable Long userId) {

        return ResponseEntity.ok(adminService.getUserById(userId));
    }

    /**
     * Block a user
     */
    @PatchMapping("/users/{userId}/block")
    public ResponseEntity<String> blockUser(
            @PathVariable Long userId) {

        adminService.blockUser(userId);
        return ResponseEntity.ok("User blocked successfully.");
    }

    /**
     * Unblock a user
     */
    @PatchMapping("/users/{userId}/unblock")
    public ResponseEntity<String> unblockUser(
            @PathVariable Long userId) {

        adminService.unblockUser(userId);
        return ResponseEntity.ok("User unblocked successfully.");
    }

    /**
     * Get pending seller approvals
     */
    @GetMapping("/sellers/pending")
    public ResponseEntity<List<PendingSellerDto>> getPendingSellers() {
        return ResponseEntity.ok(adminService.getPendingSellers());
    }

    /**
     * Approve seller
     */
    @PatchMapping("/sellers/{sellerId}/approve")
    public ResponseEntity<String> approveSeller(
            @PathVariable Long sellerId) {

        adminService.approveSeller(sellerId);
        return ResponseEntity.ok("Seller approved successfully.");
    }
}
