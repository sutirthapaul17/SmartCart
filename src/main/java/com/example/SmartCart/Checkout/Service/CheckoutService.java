package com.example.SmartCart.Checkout.Service;

import com.example.SmartCart.Checkout.Dto.Response.CheckoutResponseDto;

import java.util.List;

public interface CheckoutService {
    CheckoutResponseDto calculateCheckout(
            Long shippingAddressId,
            List<Long> cartItemIds
    );
}
