package com.example.SmartCart.Products.Repository;

import com.example.SmartCart.Products.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}