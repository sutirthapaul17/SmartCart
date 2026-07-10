package com.example.SmartCart.Mapper.User;

import com.example.SmartCart.User.Dto.User.CreateUserDto;
import com.example.SmartCart.User.Dto.User.UserResponseDto;
import com.example.SmartCart.User.Dto.User.UserUpdateDto;
import com.example.SmartCart.User.Entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDto toUserResponseDto(User user);

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="dateOfBirth",source = "dob")
    void updateUserFromDto(UserUpdateDto dto, @MappingTarget User user);

    User toUser(CreateUserDto dto);
}
