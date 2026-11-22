package com.example.SmartCart.Cart.Service;

import com.example.SmartCart.Cart.Dto.Request.AddCartItemReq;
import com.example.SmartCart.Cart.Dto.Request.UpdateCartItemReq;
import com.example.SmartCart.Cart.Dto.Response.CartResponse;

public interface CartService {
    CartResponse getMyCart(Long userId);

    CartResponse addItem(
            Long userId,
            AddCartItemReq request
    );

    CartResponse updateQuantity(
            Long userId,
            Long productId,
            UpdateCartItemReq request
    );

    CartResponse removeItem(
            Long userId,
            Long productId
    );

    CartResponse clearCart(Long userId);


}
