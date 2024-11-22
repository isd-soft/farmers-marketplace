package com.example.isdfarmersmarket.dao.repositories;

import com.example.isdfarmersmarket.dao.models.ProductReview;
import com.example.isdfarmersmarket.dao.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
    Page<ProductReview> findByCreator(User creator, Pageable pageable);
}
