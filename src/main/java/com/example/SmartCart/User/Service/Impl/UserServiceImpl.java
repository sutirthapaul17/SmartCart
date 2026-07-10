package com.example.SmartCart.User.Service.Impl;

import com.example.SmartCart.Mapper.User.UserMapper;
import com.example.SmartCart.User.Dto.User.CreateUserDto;
import com.example.SmartCart.User.Dto.User.UserResponseDto;
import com.example.SmartCart.User.Dto.User.UserUpdateDto;
import com.example.SmartCart.User.Entity.User;
import com.example.SmartCart.User.Repository.UserRepo;
import com.example.SmartCart.User.Service.UserService;
import com.example.SmartCart.User.enums.UserRole;
import com.example.SmartCart.User.enums.UserStatus;
import com.example.SmartCart.common.Exception.ConflictException;
import com.example.SmartCart.common.Exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponseDto createUser(CreateUserDto dto) {
        if (userRepo.existsByEmail(dto.email())) {
            throw new ConflictException(
                    "User with email " + dto.email() + " already exists."
            );
        }

        if (userRepo.existsByPhoneNumber(dto.phoneNumber())) {
            throw new ConflictException(
                    "User with phone number " + dto.phoneNumber() + " already exists."
            );
        }
        User user = userMapper.toUser(dto);
        // Default values
        user.setRole(UserRole.CUSTOMER);
        user.setStatus(UserStatus.ACTIVE);
        user.setDeleted(false);
        user.setEmailVerified(true);
        user.setLastLoginAt(LocalDateTime.now());

        if (user.getAddresses() != null) {
            user.getAddresses().forEach(address -> address.setUser(user));
        }
        User savedUser = userRepo.save(user);
        return userMapper.toUserResponseDto(savedUser);
    }

    @Override
    public List<UserResponseDto> getAllUser() {
        return userRepo.findAll()
                .stream()
                .map(userMapper::toUserResponseDto)
                .toList();
    }

    @Override
    public UserResponseDto getUserById(long userId) {

        User user = userRepo.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + userId));

        return userMapper.toUserResponseDto(user);
    }

    @Override
    public UserResponseDto updateUser(long userId, UserUpdateDto userUpdateDto) {

        User user = userRepo.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + userId));

        userMapper.updateUserFromDto(userUpdateDto, user);

        User updatedUser = userRepo.save(user);

        return userMapper.toUserResponseDto(updatedUser);
    }

//    @Override
//    public UserResponseDto updateUserPass(long userId, UserUpdateDto userUpdateDto) {
//
//        User user = userRepo.findById(userId)
//                .orElseThrow(() ->
//                        new ResourceNotFoundException("User not found with id: " + userId));
//
//        user.setPassword(userUpdateDto.password());
//
//        User updatedUser = userRepo.save(user);
//
//        return userMapper.toUserResponseDto(updatedUser);
//    }

    @Override
    public void deleteUser(Long userId) {

        User user = userRepo.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + userId));

        userRepo.delete(user);

        // return "User with id " + userId + " deleted successfully.";
    }


}
