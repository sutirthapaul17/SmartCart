package com.example.SmartCart.User.Controller;


import com.example.SmartCart.User.Dto.User.userUpdateDto;
import com.example.SmartCart.User.Entity.User;
import com.example.SmartCart.User.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping()
    public List<User> getAllUser(){
        return userService.getAllUser();
    }

    @GetMapping("/getUser/{userId}")
    public List<User> getUserById(@PathVariable long userId){
        return userService.getUserById(userId);
    }

    @PutMapping("/users/{userId}")
    public userUpdateDto updateUserProfile(@PathVariable long userId,
                                  @RequestBody userUpdateDto userUpdateDto){
        return userService.updateUser(userId,userUpdateDto);
    }

    //changePass
    @PutMapping("/changePass/{userId}")
    public userUpdateDto updateUserPass(@PathVariable long userId,
                                           @RequestBody userUpdateDto userUpdateDto){
        return userService.updateUserPass(userId,userUpdateDto);
    }


}
