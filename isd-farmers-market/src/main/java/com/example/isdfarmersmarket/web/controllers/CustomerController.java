package com.example.isdfarmersmarket.web.controllers;
import com.example.isdfarmersmarket.business.security.JwtPrincipal;
import com.example.isdfarmersmarket.business.services.CustomerService;
import com.example.isdfarmersmarket.web.commands.FarmerReviewCommand;
import com.example.isdfarmersmarket.web.commands.ProductReviewCommand;
import com.example.isdfarmersmarket.web.dto.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CustomerController {
    CustomerService customerService;
    @PreAuthorize("hasRole('CUSTOMER') and not hasRole('FARMER') ")
    @PostMapping("/review/farmer")
    public ResponseEntity<FarmerReviewDTO> rateFarmer(@RequestBody FarmerReviewCommand farmerReviewCommand){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(customerService.rateFarmer(farmerReviewCommand));
    }
    @PreAuthorize("hasRole('CUSTOMER') and not hasRole('FARMER') ")
    @PostMapping("/review/product")
    public ResponseEntity<ProductReviewDTO> rateProduct(@RequestBody ProductReviewCommand productReviewCommand)
    {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(customerService.rateProduct(productReviewCommand));
    }
    @GetMapping("/{userId}/reviews/farmer")
    public ResponseEntity<List<CustomerFarmerReviewDTO>> getAllFarmerReviews(@PathVariable Long userId,
                                                                             @RequestParam Integer page,
                                                                             @RequestParam Integer pageSize) {
            return ResponseEntity.ok(customerService.fetchAllFarmerReviews(userId, page, pageSize));
    }
    @GetMapping("/{userId}/reviews/product")
    public ResponseEntity<List<CustomerProductReviewDTO>> getAllProductReviews(@PathVariable Long userId,
                                                                               @RequestParam Integer page,
                                                                               @RequestParam Integer pageSize) {
        return ResponseEntity.ok(customerService.fetchAllProductReviews(userId, page, pageSize));
    }
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/wishlist")
    public ResponseEntity<List<CompactProductDTO>> getWishlistItems() {
        return ResponseEntity.ok(customerService.getWishlistProducts());
    }
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/wishlist/{id}")
    public ResponseEntity<CompactProductDTO> addToWishList(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(customerService.addProductToWishlist(id));
    }
    @PreAuthorize("hasRole('CUSTOMER')")
    @DeleteMapping("/wishlist/{id}")
    public ResponseEntity<CompactProductDTO> deleteFromWishlist(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(customerService.deleteProductFromWishlist(id));
    }

}