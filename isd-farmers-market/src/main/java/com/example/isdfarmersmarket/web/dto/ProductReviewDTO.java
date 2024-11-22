package com.example.isdfarmersmarket.web.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductReviewDTO {
    Long id;
    float rating;
    String content;
    Long productId;
    String productName;
    UserProfileDTO creator;
}