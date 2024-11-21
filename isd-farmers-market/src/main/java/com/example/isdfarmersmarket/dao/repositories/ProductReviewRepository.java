package com.example.isdfarmersmarket.dao.repositories;
import com.example.isdfarmersmarket.dao.models.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
}
