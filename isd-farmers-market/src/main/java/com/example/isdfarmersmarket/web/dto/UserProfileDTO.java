package com.example.isdfarmersmarket.web.dto;


import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileDTO {
    Long id;
    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    String description;
    String address;
    Float rating;
    Integer reviewCount;
    // Booleans
    Boolean isFarmer;
    Boolean isCurrentUser;
    Boolean canMessage;
    Boolean canReview;
}