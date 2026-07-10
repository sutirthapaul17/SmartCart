package com.example.SmartCart.User.Service;

import com.example.SmartCart.User.Dto.User.CreateUserDto;
import com.example.SmartCart.User.Dto.User.UserResponseDto;
import com.example.SmartCart.User.Dto.User.UserUpdateDto;

import java.util.List;

public interface UserService {
    List<UserResponseDto> getAllUser();

    UserResponseDto getUserById(long userId);

    UserResponseDto updateUser(long userId, UserUpdateDto userUpdateDto);

//    UserResponseDto updateUserPass(long userId, UserUpdateDto userUpdateDto);

    void deleteUser(Long userId);

    UserResponseDto createUser(CreateUserDto dto);
}
