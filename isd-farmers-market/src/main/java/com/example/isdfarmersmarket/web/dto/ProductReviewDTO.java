package com.example.isdfarmersmarket.web.dto;

import com.example.isdfarmersmarket.dao.models.Product;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductReviewDTO  {
    Long id;
    float rating;
    String content;
    UserProfileDTO creator;
    Product product;
}