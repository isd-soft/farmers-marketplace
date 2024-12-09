package com.example.isdfarmersmarket.web.dto;

import com.example.isdfarmersmarket.dao.enums.UnitType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductPageDTO {
    Long id;
    String title;
    String description;
    UnitType unitType;
    BigDecimal pricePerUnit;
    Integer discountPercents;
    Integer quantity;
    UserDTO farmer;
    Float rating;
    private boolean visible;
    Integer reviewCount;
    Boolean canReview;
    Boolean isInWishlist;
    List<ImageDTO> images = new ArrayList<>();
}
