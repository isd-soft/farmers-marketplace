package com.example.isdfarmersmarket.dao.repositories;

import com.example.isdfarmersmarket.dao.models.FarmerReview;
import com.example.isdfarmersmarket.dao.models.User;
import com.example.isdfarmersmarket.web.dto.ReviewStatsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FarmerReviewRepository extends JpaRepository<FarmerReview, Long> {
    Page<FarmerReview> findByCreatorOrderByCreatedDate(User creator, Pageable pageable);
    Page<FarmerReview> findByFarmerOrderByCreatedDateDesc(User farmer, Pageable pageable);
    @Query("SELECT new com.example.isdfarmersmarket.web.dto.ReviewStatsDTO(AVG(r.rating), COUNT(r)) " +
            "FROM FarmerReview r WHERE r.farmer = :farmer")
    ReviewStatsDTO findReviewStatsByFarmer(@Param("farmer") User farmer);
}
