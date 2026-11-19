package com.example.SmartCart.Order.Repository;

import com.example.SmartCart.Order.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}