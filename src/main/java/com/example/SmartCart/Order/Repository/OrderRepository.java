package com.example.SmartCart.Order.Repository;

import com.example.SmartCart.Order.Entity.Order;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer_IdOrderByCreatedAtDesc(Long customerId);

    Optional<Order> findByIdAndCustomer_Id(
            Long orderId,
            Long customerId
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT o
            FROM Order o
            WHERE o.id = :orderId
            AND o.customer.id = :customerId
            """)
    Optional<Order> findByIdAndCustomer_IdForUpdate(
            @Param("orderId") Long orderId,
            @Param("customerId") Long customerId
    );
}