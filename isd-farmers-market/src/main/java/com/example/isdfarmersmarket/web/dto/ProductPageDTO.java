package com.example.isdfarmersmarket.web.dto;

import com.example.isdfarmersmarket.dao.enums.UnitType;
import com.example.isdfarmersmarket.dao.models.Product;
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
    Integer reviewCount;
    Boolean canReview;
    Boolean isInWishlist;
    List<ImageDTO> images = new ArrayList<>();

}
