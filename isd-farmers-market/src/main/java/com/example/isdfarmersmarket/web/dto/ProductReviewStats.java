package com.example.isdfarmersmarket.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductReviewStats {
    private double averageRating;
    private long reviewCount;
}
