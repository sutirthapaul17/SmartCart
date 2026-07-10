package com.example.SmartCart.Mapper.User;

import com.example.SmartCart.User.Dto.Seller.PendingSellerDto;
import com.example.SmartCart.User.Dto.Seller.SellerProfileRequestDto;
import com.example.SmartCart.User.Dto.Seller.SellerProfileResponseDto;
import com.example.SmartCart.User.Entity.SellerProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SellerMapper {

    SellerProfile toEntity(SellerProfileRequestDto dto);

    SellerProfileResponseDto toResponseDto(SellerProfile seller);

    void updateSellerFromDto(SellerProfileRequestDto dto,
                             @MappingTarget SellerProfile seller);

    @Mapping(target = "sellerId", source = "id")
    @Mapping(target = "fullName",
            expression = "java(sellerProfile.getUser().getFirstName() + \" \" + sellerProfile.getUser().getLastName())")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "businessName", source = "storeName")
    @Mapping(target = "phone", source = "businessPhone")
    PendingSellerDto toPendingSellerDto(SellerProfile seller);
}
