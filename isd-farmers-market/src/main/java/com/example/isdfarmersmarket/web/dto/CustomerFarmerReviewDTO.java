package com.example.isdfarmersmarket.web.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerFarmerReviewDTO {
    Long id;
    float rating;
    String content;
    UserProfileDTO farmer;
}
