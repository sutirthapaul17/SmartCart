package com.example.SmartCart.Order.Repository;

import com.example.SmartCart.Order.Entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}