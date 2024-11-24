package com.example.isdfarmersmarket.dao.repositories;

import com.example.isdfarmersmarket.dao.models.Product;
import com.example.isdfarmersmarket.dao.models.ProductReview;
import com.example.isdfarmersmarket.dao.models.User;
import com.example.isdfarmersmarket.web.dto.ProductReviewStats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
    Page<ProductReview> findByCreatorOrderByCreatedDate(User creator, Pageable pageable);
    Page<ProductReview> findAllByProduct(Product product, Pageable pageable);
    @Query("SELECT new com.example.isdfarmersmarket.web.dto.ProductReviewStats(AVG(r.rating), COUNT(r)) " +
            "FROM ProductReview r WHERE r.product = :product")
    ProductReviewStats findReviewStatsByProduct(@Param("product") Product product);

}