package com.example.isdfarmersmarket.dao.repositories;

import com.example.isdfarmersmarket.dao.models.FarmerReview;
import com.example.isdfarmersmarket.dao.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FarmerReviewRepository extends JpaRepository<FarmerReview, Long> {
    Page<FarmerReview> findByCreator(User creator, Pageable pageable);

}
