package com.example.SmartCart.User.Service.Impl;

import com.example.SmartCart.User.Dto.User.userUpdateDto;
import com.example.SmartCart.User.Entity.User;
import com.example.SmartCart.User.Service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {
    @Override
    public List<User> getAllUser() {
        return List.of();
    }

    @Override
    public List<User> getUserById(long userId) {
        return List.of();
    }

    @Override
    public userUpdateDto updateUser(long userId, userUpdateDto userUpdateDto) {
        return null;
    }

    @Override
    public userUpdateDto updateUserPass(long userId, userUpdateDto userUpdateDto) {
        return null;
    }
}
