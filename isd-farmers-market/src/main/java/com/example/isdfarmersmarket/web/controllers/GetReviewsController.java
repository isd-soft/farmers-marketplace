package com.example.isdfarmersmarket.web.controllers;

import com.example.isdfarmersmarket.business.services.interfaces.GetReviewsService;
import com.example.isdfarmersmarket.web.dto.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GetReviewsController {

    GetReviewsService reviewQueryService;

    @GetMapping("/products/{productId}")
    public ResponseEntity<PageResponseDTO<ProductReviewDTO>> getProductReviews(
            @PathVariable Long productId,
            Pageable pageable) {
        PageResponseDTO<ProductReviewDTO> response = reviewQueryService.getProductReviews(productId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/farmers/{farmerId}")
    public ResponseEntity<PageResponseDTO<FarmerReviewDTO>> getFarmerReviews(
            @PathVariable Long farmerId,
            Pageable pageable) {
        PageResponseDTO<FarmerReviewDTO> response = reviewQueryService.getFarmerReviews(farmerId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customers/{customerId}/product-reviews")
    public ResponseEntity<PageResponseDTO<CustomerProductReviewDTO>> fetchAllProductReviewsForCustomer(
            @PathVariable Long customerId,
            Pageable pageable) {
        PageResponseDTO<CustomerProductReviewDTO> response = reviewQueryService.fetchAllProductReviewsForCustomer(customerId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customers/{customerId}/farmer-reviews")
    public ResponseEntity<PageResponseDTO<CustomerFarmerReviewDTO>> fetchAllFarmerReviewsForCustomer(
            @PathVariable Long customerId,
            Pageable pageable) {
        PageResponseDTO<CustomerFarmerReviewDTO> response = reviewQueryService.fetchAllFarmerReviewsForCustomer(customerId, pageable);
        return ResponseEntity.ok(response);
    }
}
