package com.example.SmartCart.User.Controller;


import com.example.SmartCart.User.Dto.User.CreateUserDto;
import com.example.SmartCart.User.Dto.User.UserResponseDto;
import com.example.SmartCart.User.Dto.User.UserUpdateDto;
import com.example.SmartCart.User.Entity.User;
import com.example.SmartCart.User.Service.UserService;
import com.example.SmartCart.common.Handler.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping()
    public ResponseEntity<ApiResponse<UserResponseDto>> createUser(@Valid @RequestBody CreateUserDto dto){
        UserResponseDto user = userService.createUser(dto);

        ApiResponse<UserResponseDto> response = new ApiResponse<>(
                "User created successfully",
                user
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    @GetMapping()
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getAllUser(){
        List<UserResponseDto> users = userService.getAllUser();
        ApiResponse<List<UserResponseDto>> response = new ApiResponse<>("Users retrieved successfully", users);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getUser/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserById(
            @PathVariable long userId
    ){
        UserResponseDto user = userService.getUserById(userId);
        ApiResponse<UserResponseDto> response = new ApiResponse<>(
                "User retrieved successfully",
                user
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUserProfile(
            @PathVariable long userId,
            @Valid @RequestBody UserUpdateDto userUpdateDto
    ){
        UserResponseDto user = userService.updateUser(userId,userUpdateDto);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "User updated Successfully",
                        user
                )
        );
    }

    //changePass
//    @PutMapping("/changePass/{userId}")
//    public UserResponseDto updateUserPass(@PathVariable long userId,
//                                        @RequestBody userUpdateDto userUpdateDto){
//        return userService.updateUserPass(userId,userUpdateDto);
//    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable Long userId
    ){
        userService.deleteUser(userId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "User deleted Successfully",
                        null
                )
        );
    }
}