package com.example.isdfarmersmarket.business.services;

import com.example.isdfarmersmarket.business.mapper.ReviewMapper;
import com.example.isdfarmersmarket.dao.models.Product;
import com.example.isdfarmersmarket.dao.models.User;
import com.example.isdfarmersmarket.dao.repositories.FarmerReviewRepository;
import com.example.isdfarmersmarket.dao.repositories.UserRepository;
import com.example.isdfarmersmarket.web.dto.FarmerReviewDTO;
import com.example.isdfarmersmarket.web.dto.PageResponseDTO;
import com.example.isdfarmersmarket.web.dto.ReviewStatsDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FarmerService {
    UserRepository userRepository;
    FarmerReviewRepository farmerReviewRepository;
    ReviewMapper reviewMapper;

    @Transactional(readOnly = true)
    public PageResponseDTO<FarmerReviewDTO> getFarmerReviews(Long farmerId, int page, int pageSize) {
        User farmer = userRepository.findById(farmerId).orElseThrow(EntityNotFoundException::new);
        var reviewsPage = farmerReviewRepository
                .findAllByFarmerOrderByCreatedDateDesc(farmer, PageRequest.of(page, pageSize));
        var totalReviews = reviewsPage.getTotalElements();
        var content = reviewsPage
                .getContent()
                .stream()
                .map(reviewMapper::map)
                .toList();
        return new PageResponseDTO<>(content, totalReviews, page, pageSize);
    }
    public void updateFarmerRating(User farmer){
        ReviewStatsDTO reviewStatsDTO = farmerReviewRepository
                .findReviewStatsByFarmer(farmer);

        farmer.setRating(reviewStatsDTO.getAverageRating().floatValue());
        farmer.setReviewCount(reviewStatsDTO.getReviewCount().intValue());
    }

}