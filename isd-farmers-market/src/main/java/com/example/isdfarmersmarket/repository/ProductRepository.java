package com.example.isdfarmersmarket.repository;

import com.example.isdfarmersmarket.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
    Product findByTitle(String title);
}
