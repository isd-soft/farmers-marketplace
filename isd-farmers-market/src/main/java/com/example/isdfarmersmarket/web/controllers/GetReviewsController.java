package com.example.isdfarmersmarket.web.controllers;

import com.example.isdfarmersmarket.business.services.interfaces.GetReviewsService;
import com.example.isdfarmersmarket.web.dto.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GetReviewsController {

    GetReviewsService getReviewsService;

    @GetMapping("/products/{productId}")
    public ResponseEntity<PageResponseDTO<ProductReviewDTO>> getProductReviews(
            @PathVariable Long productId,
            Pageable pageable) {
        PageResponseDTO<ProductReviewDTO> response = getReviewsService.getProductReviews(productId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/allproducts")
    public ResponseEntity<List<ProductReviewDTO>> getAllProductReviews() {
        List<ProductReviewDTO> response = getReviewsService.getAllProductReviews();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/allfarmers")
    public ResponseEntity<List<FarmerReviewDTO>> getAllFarmerReviews() {
        List<FarmerReviewDTO> response = getReviewsService.getAllFarmerReviews();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/farmers/{farmerId}")
    public ResponseEntity<PageResponseDTO<FarmerReviewDTO>> getFarmerReviews(
            @PathVariable Long farmerId,
            Pageable pageable) {
        PageResponseDTO<FarmerReviewDTO> response = getReviewsService.getFarmerReviews(farmerId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customers/{customerId}/product-reviews")
    public ResponseEntity<PageResponseDTO<CustomerProductReviewDTO>> fetchAllProductReviewsForCustomer(
            @PathVariable Long customerId,
            Pageable pageable) {
        PageResponseDTO<CustomerProductReviewDTO> response = getReviewsService.fetchAllProductReviewsForCustomer(customerId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customers/{customerId}/farmer-reviews")
    public ResponseEntity<PageResponseDTO<CustomerFarmerReviewDTO>> fetchAllFarmerReviewsForCustomer(
            @PathVariable Long customerId,
            Pageable pageable) {
        PageResponseDTO<CustomerFarmerReviewDTO> response = getReviewsService.fetchAllFarmerReviewsForCustomer(customerId, pageable);
        return ResponseEntity.ok(response);
    }
}
