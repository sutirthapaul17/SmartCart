package com.example.SmartCart.Order.Entity;

import com.example.SmartCart.Products.Entity.Product;
import com.example.SmartCart.User.Entity.SellerProfile;
import com.example.SmartCart.common.Entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private SellerProfile seller;

    // Product details at the time of purchase
    @Column(nullable = false, length = 150)
    private String productName;

    @Column(length = 1000)
    private String productImage;

    @Column(nullable = false)
    private Integer quantity;

    /**
     * Product price at the time of purchase.
     */
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalPrice;
}
