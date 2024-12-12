package com.example.isdfarmersmarket.dao.repositories;

import com.example.isdfarmersmarket.dao.models.Product;
import com.example.isdfarmersmarket.dao.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Page<Product> findProductsByFarmer(User farmer, Pageable pageable);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.category.id = :categoryId")
    Integer countByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT p FROM Product p WHERE p.discountPercents >= 50 ORDER BY p.rating DESC")
    Page<Product> findDiscountedAbove50(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.discountPercents >= 30 AND p.discountPercents < 50 ORDER BY p.rating DESC")
    Page<Product> findDiscountedAbove30AndBelow50(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.discountPercents >= 15 AND p.discountPercents < 30 ORDER BY p.rating DESC")
    Page<Product> findDiscountedAbove15AndBelow30(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.discountPercents >= 5 AND p.discountPercents < 15 ORDER BY p.rating DESC")
    Page<Product> findDiscountedAbove5AndBelow15(Pageable pageable);

    boolean existsByCategoryId(Long id);
}
