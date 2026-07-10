package com.example.SmartCart.Mapper.User;

import com.example.SmartCart.User.Dto.Address.AddressRequestDto;
import com.example.SmartCart.User.Dto.Address.AddressResponseDto;
import com.example.SmartCart.User.Entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address toEntity(AddressRequestDto dto);

    AddressResponseDto toResponseDto(Address address);

    void updateEntityFromDto(AddressRequestDto dto, @MappingTarget Address address);
}
