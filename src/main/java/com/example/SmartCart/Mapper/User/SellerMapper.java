package com.example.SmartCart.Mapper.User;

import com.example.SmartCart.User.Dto.Seller.PendingSellerDto;
import com.example.SmartCart.User.Dto.Seller.SellerProfileRequestDto;
import com.example.SmartCart.User.Dto.Seller.SellerProfileResponseDto;
import com.example.SmartCart.User.Entity.SellerProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface SellerMapper {

    SellerProfile toEntity(SellerProfileRequestDto dto);

    SellerProfileResponseDto toResponseDto(SellerProfile seller);

    void updateSellerFromDto(SellerProfileRequestDto dto,
                             @MappingTarget SellerProfile seller);

    @Mapping(target = "sellerId", source = "id")
    @Mapping(target = "fullName", source = ".", qualifiedByName = "fullName")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "businessName", source = "storeName")
    @Mapping(target = "phone", source = "businessPhone")
    PendingSellerDto toPendingSellerDto(SellerProfile seller);

    @Named("fullName")
    default String fullName(SellerProfile seller) {
        return seller.getUser().getFirstName() + " " + seller.getUser().getLastName();
    }
}
