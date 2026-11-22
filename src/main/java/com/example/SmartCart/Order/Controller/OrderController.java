
package com.example.SmartCart.Order.Controller;

import com.example.SmartCart.Order.Dto.Request.CreateOrderRequestDto;
import com.example.SmartCart.Order.Dto.Response.OrderResponseDto;
import com.example.SmartCart.Order.Dto.Response.OrderSummaryResponseDto;
import com.example.SmartCart.Order.Service.OrderService;
import com.example.SmartCart.common.Handler.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@PreAuthorize("hasRole('CUSTOMER')")
@Tag(
        name = "Order Management",
        description = "APIs for placing orders, viewing order history, cancelling orders and downloading invoices."
)
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(
            summary = "Place a New Order",
            description = "Creates an order from selected cart items and the customer's shipping address."
    )
    public ResponseEntity<ApiResponse<OrderResponseDto>> createOrder(
            @Valid @RequestBody CreateOrderRequestDto request) {

        OrderResponseDto response = orderService.createOrder(request);

        return ResponseEntity.status(201).body(
                ApiResponse.<OrderResponseDto>builder()
                        .message("Order created successfully.")
                        .data(response)
                        .build()
        );
    }

    @GetMapping
    @Operation(
            summary = "Get My Order History",
            description = "Returns the authenticated customer's orders, newest first."
    )
    public ResponseEntity<ApiResponse<List<OrderSummaryResponseDto>>> getMyOrders() {

        List<OrderSummaryResponseDto> response =
                orderService.getMyOrders();

        return ResponseEntity.ok(
                ApiResponse.<List<OrderSummaryResponseDto>>builder()
                        .message("Orders fetched successfully.")
                        .data(response)
                        .build()
        );
    }

    @GetMapping("/{orderId}")
    @Operation(
            summary = "Get Order Details",
            description = "Returns the full details of an order belonging to the authenticated customer."
    )
    public ResponseEntity<ApiResponse<OrderResponseDto>> getOrderById(
            @PathVariable Long orderId) {

        OrderResponseDto response =
                orderService.getOrderById(orderId);

        return ResponseEntity.ok(
                ApiResponse.<OrderResponseDto>builder()
                        .message("Order details fetched successfully.")
                        .data(response)
                        .build()
        );
    }

    @PatchMapping("/{orderId}/cancel")
    @Operation(
            summary = "Cancel My Order",
            description = "Cancels an eligible order and restores the purchased product stock."
    )
    public ResponseEntity<ApiResponse<OrderResponseDto>> cancelOwnOrder(
            @PathVariable Long orderId) {

        OrderResponseDto response =
                orderService.cancelOwnOrder(orderId);

        return ResponseEntity.ok(
                ApiResponse.<OrderResponseDto>builder()
                        .message("Order cancelled successfully.")
                        .data(response)
                        .build()
        );
    }

    @GetMapping(
            value = "/{orderId}/invoice",
            produces = MediaType.APPLICATION_PDF_VALUE
    )
    @Operation(
            summary = "Download Order Invoice",
            description = "Generates and downloads a PDF invoice for an order belonging to the authenticated customer."
    )
    public ResponseEntity<byte[]> downloadInvoice(
            @PathVariable Long orderId) {

        byte[] pdf = orderService.downloadInvoice(orderId);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=SmartCart-Invoice-" +
                                orderId + ".pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdf.length)
                .body(pdf);
    }
}
