package com.example.isdfarmersmarket.web.controllers;

import com.example.isdfarmersmarket.business.services.interfaces.CreateReviewsService;
import com.example.isdfarmersmarket.web.commands.FarmerReviewCommand;
import com.example.isdfarmersmarket.web.commands.ProductReviewCommand;
import com.example.isdfarmersmarket.web.dto.FarmerReviewDTO;
import com.example.isdfarmersmarket.web.dto.ProductReviewDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CreateReviewsController {

    CreateReviewsService reviewCommandService;

    @PreAuthorize("hasRole('CUSTOMER') and not hasRole('FARMER')")
    @PostMapping("/farmers")
    public ResponseEntity<FarmerReviewDTO> rateFarmer(@RequestBody @Valid FarmerReviewCommand farmerReviewCommand) {
        FarmerReviewDTO response = reviewCommandService.rateFarmer(farmerReviewCommand);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('CUSTOMER') and not hasRole('FARMER')")
    @PostMapping("/products")
    public ResponseEntity<ProductReviewDTO> rateProduct(@RequestBody @Valid ProductReviewCommand productReviewCommand) {
        ProductReviewDTO response = reviewCommandService.rateProduct(productReviewCommand);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @DeleteMapping("/farmers/{reviewId}")
    public ResponseEntity<Void> deleteFarmerReview(@PathVariable Long reviewId) {
        reviewCommandService.deleteFarmerReview(reviewId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @DeleteMapping("/products/{reviewId}")
    public ResponseEntity<Void> deleteProductReview(@PathVariable Long reviewId) {
        reviewCommandService.deleteProductReview(reviewId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
