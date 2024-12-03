package com.example.isdfarmersmarket.web.controllers;

import com.example.isdfarmersmarket.business.services.WishlistServiceImpl;
import com.example.isdfarmersmarket.web.dto.CompactProductDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishlist")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WishlistController {

    WishlistServiceImpl wishlistService;

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping
    public ResponseEntity<List<CompactProductDTO>> getWishlistItems() {
        return ResponseEntity.ok(wishlistService.getWishlistProducts());
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/{id}")
    public ResponseEntity<CompactProductDTO> addToWishList(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(wishlistService.addProductToWishlist(id));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<CompactProductDTO> deleteFromWishlist(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(wishlistService.deleteProductFromWishlist(id));
    }
}
