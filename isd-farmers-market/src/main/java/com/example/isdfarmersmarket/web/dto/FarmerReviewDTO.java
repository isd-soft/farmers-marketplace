package com.example.isdfarmersmarket.web.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FarmerReviewDTO {
    Long id;
    float rating;
    String content;
    UserProfileDTO farmer;
    UserProfileDTO creator;
}