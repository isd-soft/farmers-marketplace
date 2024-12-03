package com.example.isdfarmersmarket.web.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompactUserDTO {
    Long id;
    String firstName;
    String lastName;
    Boolean isFarmer;
}
