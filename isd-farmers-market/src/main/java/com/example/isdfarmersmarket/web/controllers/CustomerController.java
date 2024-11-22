package com.example.isdfarmersmarket.web.controllers;
import com.example.isdfarmersmarket.business.services.CustomerService;
import com.example.isdfarmersmarket.web.commands.FarmerReviewCommand;
import com.example.isdfarmersmarket.web.commands.ProductReviewCommand;
import com.example.isdfarmersmarket.web.dto.FarmerReviewDTO;
import com.example.isdfarmersmarket.web.dto.ProductReviewDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CustomerController {
    CustomerService customerService;
    @PreAuthorize("hasRole('CUSTOMER') and not hasRole('FARMER') ")
    @PostMapping("/review/farmer")
    public ResponseEntity<FarmerReviewDTO> rateFarmer(@RequestBody FarmerReviewCommand farmerReviewCommand,
                                                      Authentication authentication
    ){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(customerService.rateFarmer(farmerReviewCommand, authentication.getName()));
    }
    @PreAuthorize("hasRole('CUSTOMER') and not hasRole('FARMER') ")
    @PostMapping("/review/product")
    public ResponseEntity<ProductReviewDTO> rateProduct(@RequestBody ProductReviewCommand productReviewDTO,
                                                        Authentication authentication)
    {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(customerService.rateProduct(productReviewDTO,authentication.getName()));
    }
    @GetMapping("/{userId}/reviews/farmer")
    public ResponseEntity<List<FarmerReviewDTO>> getAllFarmerReviews(@PathVariable Long userId,
                                                                     @RequestParam Integer page,
                                                                     @RequestParam Integer pageSize) {
        return ResponseEntity.ok(customerService.fetchAllFarmerReviews(userId, page, pageSize));
    }
    @GetMapping("/{userId}/reviews/product")
    public ResponseEntity<List<ProductReviewDTO>> getAllProductReviews(@PathVariable Long userId,
                                                                       @RequestParam Integer page,
                                                                       @RequestParam Integer pageSize) {
        return ResponseEntity.ok(customerService.fetchAllProductReviews(userId, page, pageSize));
    }

}