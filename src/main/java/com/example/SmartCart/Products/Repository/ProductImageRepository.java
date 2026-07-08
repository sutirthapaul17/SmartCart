package com.example.SmartCart.Products.Repository;

import com.example.SmartCart.Products.Entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
}