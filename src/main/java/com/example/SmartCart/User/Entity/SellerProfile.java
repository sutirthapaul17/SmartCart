package com.example.SmartCart.User.Entity;


import com.example.SmartCart.User.enums.SellerStatus;
import com.example.SmartCart.common.Entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerProfile extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String storeName;

    private String storeDescription;

    private String gstNumber;

    private String businessPhone;

    @Enumerated(EnumType.STRING)
    private SellerStatus sellerStatus;

    @Column(nullable = false)
    private String panNumber;
}
