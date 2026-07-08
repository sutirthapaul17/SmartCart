package com.example.SmartCart.User.Service;

import com.example.SmartCart.User.Dto.User.userUpdateDto;
import com.example.SmartCart.User.Entity.User;

import java.util.List;

public interface UserService {
    List<User> getAllUser();


    List<User> getUserById(long userId);

    userUpdateDto updateUser(long userId, userUpdateDto userUpdateDto);

    userUpdateDto updateUserPass(long userId, userUpdateDto userUpdateDto);

}
