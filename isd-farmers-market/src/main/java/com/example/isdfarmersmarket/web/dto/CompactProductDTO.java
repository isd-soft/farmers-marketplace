package com.example.isdfarmersmarket.web.dto;

import com.example.isdfarmersmarket.dao.enums.UnitType;
import com.example.isdfarmersmarket.dao.models.Category;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompactProductDTO {
    Long id;
    String title;
    String description;
    BigDecimal pricePerUnit;
    int quantity;
    UnitType unitType;
    private boolean visible;
    String unitTypeShort;
    int discountPercents;
    Boolean isInWishlist;
    Float rating;
    ImageDTO image;
    long orders;
    CategoryDTO category;
}
