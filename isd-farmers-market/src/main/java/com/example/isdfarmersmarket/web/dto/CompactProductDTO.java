package com.example.isdfarmersmarket.web.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompactProductDTO {
    Long id;
    private String title;
    private String description;
    private BigDecimal pricePerUnit;
    private int discountPercents;
    private Float rating;
    private ImageDTO image;
}
