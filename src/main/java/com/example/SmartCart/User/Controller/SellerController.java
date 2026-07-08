package com.example.SmartCart.User.Controller;


import com.example.SmartCart.User.Dto.Seller.SellerProfileRequestDto;
import com.example.SmartCart.User.Dto.Seller.SellerProfileResponseDto;
import com.example.SmartCart.User.Service.SellerProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller/profile")
public class SellerController {

    private final SellerProfileService sellerProfileService;

    @GetMapping
    public ResponseEntity<SellerProfileResponseDto> getSellerProfile() {
        return ResponseEntity.ok(sellerProfileService.getSellerProfile());
    }

    @PutMapping
    public ResponseEntity<SellerProfileResponseDto> updateSellerProfile(
            @Valid @RequestBody SellerProfileRequestDto request) {

        return ResponseEntity.ok(
                sellerProfileService.updateSellerProfile(request)
        );
    }
}
