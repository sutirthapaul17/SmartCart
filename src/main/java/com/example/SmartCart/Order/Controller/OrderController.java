package com.example.SmartCart.Order.Controller;

import com.example.SmartCart.Order.Dto.Request.CreateOrderRequestDto;
import com.example.SmartCart.Order.Dto.Response.OrderResponseDto;
import com.example.SmartCart.Order.Dto.Response.OrderSummaryResponseDto;
import com.example.SmartCart.Order.Service.OrderService;
import com.example.SmartCart.common.Handler.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasAnyRole('CUSTOMER', 'SELLER','ADMIN')")
    public ResponseEntity<ApiResponse<OrderResponseDto>> createOrder(
            @Valid @RequestBody CreateOrderRequestDto request) {

        OrderResponseDto response = orderService.createOrder(request);

        return ResponseEntity.ok(
                ApiResponse.<OrderResponseDto>builder()
                        .message("Order created successfully.")
                        .data(response)
                        .build()
        );
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('CUSTOMER', 'SELLER','ADMIN')")
    public ResponseEntity<ApiResponse<List<OrderSummaryResponseDto>>> getMyOrders() {

        List<OrderSummaryResponseDto> response = orderService.getMyOrders();

        return ResponseEntity.ok(
                ApiResponse.<List<OrderSummaryResponseDto>>builder()
                        .message("Orders fetched successfully.")
                        .data(response)
                        .build()
        );
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'SELLER','ADMIN')")
    public ResponseEntity<ApiResponse<OrderResponseDto>> getOrderById(
            @PathVariable Long orderId) {

        OrderResponseDto response = orderService.getOrderById(orderId);

        return ResponseEntity.ok(
                ApiResponse.<OrderResponseDto>builder()
                        .message("Order details fetched successfully.")
                        .data(response)
                        .build()
        );
    }

    @PatchMapping("/{orderId}/cancel")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'SELLER','ADMIN')")
    public ResponseEntity<ApiResponse<OrderResponseDto>> cancelOwnOrder(
            @PathVariable Long orderId) {

        OrderResponseDto response = orderService.cancelOwnOrder(orderId);

        return ResponseEntity.ok(
                ApiResponse.<OrderResponseDto>builder()
                        .message("Order cancelled successfully.")
                        .data(response)
                        .build()
        );
    }


    @GetMapping("/{orderId}/invoice")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'SELLER','ADMIN')")
    public ResponseEntity<byte[]> downloadInvoice(
            @PathVariable Long orderId) {

        byte[] pdf = orderService.downloadInvoice(orderId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=invoice.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

}
