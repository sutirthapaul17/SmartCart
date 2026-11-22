package com.example.SmartCart.Products.Repository;

import com.example.SmartCart.Products.Entity.Product;
import com.example.SmartCart.Products.enums.ProductStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByStatus(
            ProductStatus status,
            Pageable pageable
    );

    Page<Product> findByNameContainingIgnoreCaseAndStatus(
            String keyword,
            ProductStatus status,
            Pageable pageable
    );


    Page<Product> findBySeller_Id(Long sellerId, Pageable pageable);

    Optional<Product> findByIdAndSeller_Id(Long productId, Long sellerId);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id = :productId")
    Optional<Product> findByIdForUpdate(
            @Param("productId") Long productId
    );
}