package com.example.SmartCart.User.Service.Impl;

import com.example.SmartCart.Mapper.User.SellerMapper;
import com.example.SmartCart.User.Dto.Seller.SellerProfileRequestDto;
import com.example.SmartCart.User.Dto.Seller.SellerProfileResponseDto;
import com.example.SmartCart.User.Entity.SellerProfile;
import com.example.SmartCart.User.Entity.User;
import com.example.SmartCart.User.Repository.SellerProfileRepository;
import com.example.SmartCart.User.Repository.UserRepo;
import com.example.SmartCart.User.Service.SellerProfileService;
import com.example.SmartCart.User.enums.SellerStatus;
import com.example.SmartCart.common.Exception.BadRequestException;
import com.example.SmartCart.common.Exception.ResourceNotFoundException;
import com.example.SmartCart.common.Handler.ApiResponse;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerProfileServiceImpl implements SellerProfileService {

    private final SellerProfileRepository sellerProfileRepository;
    private final UserRepo userRepo;
    private final SellerMapper sellerMapper;

    @Override
    @Transactional
    public SellerProfileResponseDto registerAsSeller(
            Long userId,
            SellerProfileRequestDto request) {

        User user = userRepo.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + userId));

        if (sellerProfileRepository.existsByUserId(userId)) {
            throw new BadRequestException("Seller application already exists.");
        }

        SellerProfile seller = sellerMapper.toEntity(request);

        seller.setUser(user);
        seller.setSellerStatus(SellerStatus.PENDING);

        SellerProfile savedSeller = sellerProfileRepository.save(seller);

        return sellerMapper.toResponseDto(savedSeller);
    }

    @Override
    @Transactional(readOnly = true)
    public SellerProfileResponseDto getSellerProfile(Long userId) {
        SellerProfile seller = sellerProfileRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Seller profile not found for user id: " + userId
                        ));

        return sellerMapper.toResponseDto(seller);
    }

    @Override
    @Transactional
    public SellerProfileResponseDto updateSellerProfile(
            Long userId,
            SellerProfileRequestDto request) {

        SellerProfile seller = sellerProfileRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Seller profile not found for user id: " + userId
                        ));

        sellerMapper.updateSellerFromDto(request, seller);
        SellerProfile updatedSeller = sellerProfileRepository.save(seller);
        return sellerMapper.toResponseDto(updatedSeller);
    }


}
