package com.example.SmartCart.User.Controller;


import com.example.SmartCart.User.Dto.User.CreateUserDto;
import com.example.SmartCart.User.Dto.User.UserResponseDto;
import com.example.SmartCart.User.Dto.User.UserUpdateDto;
import com.example.SmartCart.User.Entity.User;
import com.example.SmartCart.User.Service.UserService;
import com.example.SmartCart.common.Handler.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@PreAuthorize("hasAnyRole('CUSTOMER','SELLER','ADMIN')")
@Tag(
        name = "User Management",
        description = "APIs for viewing, updating and deleting the authenticated user's account"
)
public class UserController {

    private final UserService userService;

//    @PostMapping()
//    public ResponseEntity<ApiResponse<UserResponseDto>> createUser(@Valid @RequestBody CreateUserDto dto){
//        UserResponseDto user = userService.createUser(dto);
//
//        ApiResponse<UserResponseDto> response = new ApiResponse<>(
//                "User created successfully",
//                user
//        );
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(response);
//    }


//    @GetMapping()
//    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getAllUser(){
//        List<UserResponseDto> users = userService.getAllUser();
//        ApiResponse<List<UserResponseDto>> response = new ApiResponse<>("Users retrieved successfully", users);
//
//        return ResponseEntity.ok(response);
//    }

    @Operation(
            summary = "Get My Profile",
            description = """
        Retrieves the profile information of the
        currently authenticated user.

        The user is identified using the JWT token.
        No user ID needs to be provided in the request.
        """
    )
    @GetMapping("/getuser/me")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserById(
    ){
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Long userId = user.getId();
        UserResponseDto userResponse = userService.getUserById(userId);
        ApiResponse<UserResponseDto> response = new ApiResponse<>(
                "User retrieved successfully",
                userResponse
        );
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update user profile")
    @PutMapping("/update/me")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUserProfile(
            @Valid @RequestBody UserUpdateDto userUpdateDto
    ){
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Long userId = user.getId();
        UserResponseDto userResponse = userService.updateUser(userId,userUpdateDto);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "User updated Successfully",
                        userResponse
                )
        );
    }

    //changePass
//    @PutMapping("/changePass/{userId}")
//    public UserResponseDto updateUserPass(@PathVariable long userId,
//                                        @RequestBody userUpdateDto userUpdateDto){
//        return userService.updateUserPass(userId,userUpdateDto);
//    }

    @Operation(summary = "Delete user profile")
    @DeleteMapping("/delete/me")
    public ResponseEntity<ApiResponse<Void>> deleteUser(){
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Long userId = user.getId();
        userService.deleteUser(userId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "User deleted Successfully",
                        null
                )
        );
    }
}