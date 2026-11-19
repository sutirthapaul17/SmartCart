package com.example.SmartCart.Order.Service;

import com.example.SmartCart.Order.Dto.Request.CreateOrderRequestDto;
import com.example.SmartCart.Order.Dto.Response.OrderResponseDto;
import com.example.SmartCart.Order.Dto.Response.OrderSummaryResponseDto;

import java.util.List;

public interface OrderService {

    OrderResponseDto createOrder(CreateOrderRequestDto request);

    List<OrderSummaryResponseDto> getMyOrders();

    OrderResponseDto getOrderById(Long orderId);

    OrderResponseDto cancelOwnOrder(Long orderId);

    byte[] downloadInvoice(Long orderId);
}
