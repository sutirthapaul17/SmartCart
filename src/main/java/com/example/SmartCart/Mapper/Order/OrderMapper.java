package com.example.SmartCart.Mapper.Order;

import com.example.SmartCart.Order.Dto.Response.*;
import com.example.SmartCart.Order.Entity.Order;
import com.example.SmartCart.Order.Entity.OrderItem;
import com.example.SmartCart.User.Entity.User;
import com.example.SmartCart.Mapper.User.AddressMapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = AddressMapper.class
)
public interface OrderMapper {

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "productName")
    @Mapping(target = "productImage", source = "productImage")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "totalPrice", source = "totalPrice")
    OrderItemResponseDto toOrderItemResponse(OrderItem item);

    @Mapping(
            target = "fullName",
            expression = "java(user.getFirstName() + \" \" + user.getLastName())"
    )
    @Mapping(target = "email", source = "email")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    CustomerOrderResponseDto toCustomerResponse(User user);

    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "customer",
            expression = "java(toCustomerResponse(order.getCustomer()))")
    @Mapping(target = "shippingAddress", source = "shippingAddress")
    @Mapping(target = "phoneNumber",
            source = "shippingAddress.phoneNumber")
    @Mapping(target = "items", source = "orderItems")
    @Mapping(target = "gstRate", ignore = true)
    @Mapping(target = "orderedAt", source = "createdAt")
    OrderResponseDto toOrderResponse(Order order);

    @Mapping(target = "orderId", source = "id")
    @Mapping(
            target = "totalItems",
            expression = "java(order.getOrderItems().stream().mapToInt(OrderItem::getQuantity).sum())"
    )
    @Mapping(target = "orderedAt", source = "createdAt")
    OrderSummaryResponseDto toOrderSummary(Order order);
}