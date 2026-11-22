
package com.example.SmartCart.Checkout.Controller;

import com.example.SmartCart.Checkout.Dto.Response.CheckoutResponseDto;
import com.example.SmartCart.Checkout.Service.CheckoutService;
import com.example.SmartCart.Order.Dto.Request.CreateOrderRequestDto;
import com.example.SmartCart.common.Handler.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/checkout")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@Tag(
        name = "Checkout Management",
        description = "Preview order items, GST, delivery charges and final payable amount."
)
public class CheckoutController {

    private final CheckoutService checkoutService;

    @PostMapping("/preview")
    @Operation(
            summary = "Preview Checkout",
            description = "Calculates the subtotal, GST, delivery charge, discount and final payable amount for selected cart items."
    )
    public ResponseEntity<ApiResponse<CheckoutResponseDto>> previewCheckout(
            @Valid @RequestBody CreateOrderRequestDto request) {

        CheckoutResponseDto response =
                checkoutService.calculateCheckout(
                        request.shippingAddressId(),
                        request.cartItemIds()
                );

        return ResponseEntity.ok(
                ApiResponse.<CheckoutResponseDto>builder()
                        .message("Checkout calculated successfully.")
                        .data(response)
                        .build()
        );
    }
}
