package com.example.isdfarmersmarket.web.controllers;

import com.example.isdfarmersmarket.business.services.interfaces.ReviewQueryService;
import com.example.isdfarmersmarket.web.dto.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("reviews")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewQueryController {

    ReviewQueryService reviewQueryService;

    @GetMapping("/products/{productId}")
    public ResponseEntity<PageResponseDTO<ProductReviewDTO>> getProductReviews(
            @PathVariable Long productId,
            @RequestParam int page,
            @RequestParam int size) {
        PageResponseDTO<ProductReviewDTO> response = reviewQueryService.getProductReviews(productId, PageRequest.of(page, size));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/farmers/{farmerId}")
    public ResponseEntity<PageResponseDTO<FarmerReviewDTO>> getFarmerReviews(
            @PathVariable Long farmerId,
            @RequestParam int page,
            @RequestParam int size) {
        PageResponseDTO<FarmerReviewDTO> response = reviewQueryService.getFarmerReviews(farmerId, PageRequest.of(page, size));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customers/{customerId}/product-reviews")
    public ResponseEntity<PageResponseDTO<CustomerProductReviewDTO>> fetchAllProductReviewsForCustomer(
            @PathVariable Long customerId,
            @RequestParam int page,
            @RequestParam int size) {
        PageResponseDTO<CustomerProductReviewDTO> response = reviewQueryService.fetchAllProductReviewsForCustomer(customerId, PageRequest.of(page, size));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customers/{customerId}/farmer-reviews")
    public ResponseEntity<PageResponseDTO<CustomerFarmerReviewDTO>> fetchAllFarmerReviewsForCustomer(
            @PathVariable Long customerId,
            @RequestParam int page,
            @RequestParam int size) {
        PageResponseDTO<CustomerFarmerReviewDTO> response = reviewQueryService.fetchAllFarmerReviewsForCustomer(customerId, PageRequest.of(page, size));
        return ResponseEntity.ok(response);
    }
}
