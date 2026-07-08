package com.example.SmartCart.Products.Repository;

import com.example.SmartCart.Products.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}