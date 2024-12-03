package com.example.isdfarmersmarket.business.services;

import com.example.isdfarmersmarket.business.mapper.ReviewMapper;
import com.example.isdfarmersmarket.business.services.interfaces.ReviewQueryService;
import com.example.isdfarmersmarket.dao.models.Product;
import com.example.isdfarmersmarket.dao.models.User;
import com.example.isdfarmersmarket.dao.repositories.FarmerReviewRepository;
import com.example.isdfarmersmarket.dao.repositories.ProductRepository;
import com.example.isdfarmersmarket.dao.repositories.ProductReviewRepository;
import com.example.isdfarmersmarket.dao.repositories.UserRepository;
import com.example.isdfarmersmarket.web.dto.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewQueryServiceImpl implements ReviewQueryService {
    ProductReviewRepository productReviewRepository;
    ReviewMapper reviewMapper;
    UserRepository userRepository;
    ProductRepository productRepository;
    FarmerReviewRepository farmerReviewRepository;

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<ProductReviewDTO> getProductReviews(Long productId, Pageable pageable) {
        Product product = productRepository
                .findById(productId)
                .orElseThrow(()->new EntityNotFoundException("Product not found"));

        var reviewsPage = productReviewRepository
                .findAllByProductOrderByCreatedDateDesc(product, pageable);
        var totalReviews = reviewsPage.getTotalElements();
        var content = reviewsPage
                .getContent()
                .stream()
                .map(reviewMapper::mapWithoutProductDetails)
                .toList();

        return new PageResponseDTO<>(content,totalReviews);
    }


    @Transactional(readOnly = true)
    public PageResponseDTO<FarmerReviewDTO> getFarmerReviews(Long farmerId, Pageable pageable) {
        User farmer = userRepository.findById(farmerId).orElseThrow(EntityNotFoundException::new);
        var reviewsPage = farmerReviewRepository
                .findByFarmerOrderByCreatedDateDesc(farmer, pageable);
        long totalReviews = reviewsPage.getTotalElements();
        var content = reviewsPage
                .getContent()
                .stream()
                .map(reviewMapper::map)
                .toList();
        return new PageResponseDTO<>(content, totalReviews);
    }
    @Transactional(readOnly = true)
    public PageResponseDTO<CustomerProductReviewDTO> fetchAllProductReviewsForCustomer(Long id, Pageable pageable) {
        User creator = userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No such user found"));

        var reviewsPage = productReviewRepository
                .findByCreatorOrderByCreatedDate(creator, pageable);
        var totalReviews = reviewsPage.getTotalElements();
        var content = reviewsPage
                .getContent()
                .stream()
                .map(reviewMapper::mapToCustomerProductReview)
                .toList();
        return new PageResponseDTO<>(content, totalReviews);
    }

    @Transactional(readOnly = true)
    public PageResponseDTO<CustomerFarmerReviewDTO> fetchAllFarmerReviewsForCustomer(Long id, Pageable pageable) {
        User creator = userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No such user found"));

        var reviewsPage = farmerReviewRepository
                .findByCreatorOrderByCreatedDate(creator, pageable);
        var totalReviews = reviewsPage.getTotalElements();
        var content = reviewsPage
                .getContent()
                .stream()
                .map(reviewMapper::mapToCustomerFarmerReview)
                .toList();
        return new PageResponseDTO<>(content, totalReviews);

    }
}
