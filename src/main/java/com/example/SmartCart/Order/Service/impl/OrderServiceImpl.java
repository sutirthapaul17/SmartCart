package com.example.SmartCart.Order.Service.impl;

import com.example.SmartCart.Order.Dto.Request.CreateOrderRequestDto;
import com.example.SmartCart.Order.Dto.Response.OrderResponseDto;
import com.example.SmartCart.Order.Dto.Response.OrderSummaryResponseDto;
import com.example.SmartCart.Order.Service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class OrderServiceImpl implements OrderService {

    @Override
    public OrderResponseDto createOrder(CreateOrderRequestDto request) {

        return null;
    }

    @Override
    public List<OrderSummaryResponseDto> getMyOrders() {
        return null;
    }

    @Override
    public OrderResponseDto getOrderById(Long orderId) {
        return null;
    }

    @Override
    public OrderResponseDto cancelOwnOrder(Long orderId) {
        return null;
    }

    @Override
    public byte[] downloadInvoice(Long orderId) {
        return new byte[0];
    }
}
