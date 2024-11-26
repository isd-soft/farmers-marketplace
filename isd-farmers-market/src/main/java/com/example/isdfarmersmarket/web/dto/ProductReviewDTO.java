package com.example.isdfarmersmarket.web.dto;

import com.example.isdfarmersmarket.dao.models.Product;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductReviewDTO  {
    Long id;
    Float rating;
    String content;
    UserProfileDTO creator;
    String productTitle;
    Long productId;
}