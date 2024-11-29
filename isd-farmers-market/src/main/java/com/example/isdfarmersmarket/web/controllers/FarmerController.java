package com.example.isdfarmersmarket.web.controllers;

import com.example.isdfarmersmarket.business.services.FarmerService;
import com.example.isdfarmersmarket.web.dto.FarmerReviewDTO;
import com.example.isdfarmersmarket.web.dto.PageResponseDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/farmer")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class FarmerController {
    FarmerService farmerService;
    @GetMapping("/{farmerId}/reviews")
    public ResponseEntity<PageResponseDTO<FarmerReviewDTO>> getFarmerReviews(
            @PathVariable Long farmerId,
            Pageable pageable) {
        return ResponseEntity.status(OK)
                .body(farmerService.getFarmerReviews(farmerId, pageable));
    }
}
