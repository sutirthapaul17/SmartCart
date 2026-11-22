package com.example.SmartCart.Cart.Repo;

import com.example.SmartCart.Cart.Entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Repository
public interface CartItemRepo extends JpaRepository<CartItem,Long> {

    List<CartItem> findAllByCart_Id(Long cartId);

    Optional<CartItem> findByCart_IdAndProduct_Id(
            Long cartId,
            Long productId
    );

    List<CartItem> findAllByCart_IdAndIdIn(
            Long cartId,
            Collection<Long> itemIds
    );
}
