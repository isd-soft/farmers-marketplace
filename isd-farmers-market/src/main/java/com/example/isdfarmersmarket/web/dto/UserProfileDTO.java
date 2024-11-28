package com.example.isdfarmersmarket.web.dto;


import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileDTO {
    Long id;
    String firstName;
    String lastName;
    Boolean isFarmer;
    Boolean isCurrentUser;
    String email;
    String phoneNumber;
    String description;
    String address;
}