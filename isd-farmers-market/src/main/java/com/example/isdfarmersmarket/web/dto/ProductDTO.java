package com.example.isdfarmersmarket.web.dto;

import com.example.isdfarmersmarket.dao.enums.UnitType;
import com.example.isdfarmersmarket.dao.models.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    private String title;
    private String description;
    private UnitType unitType;
    private BigDecimal pricePerUnit;
    private int discountPercents;
    private int quantity;
    private CategoryDTO category;
    private UserProfileDTO user;
    private Float rating;
    private Integer reviewCount;
    private String unitTypeShort;
    private Set<ProductReviewDTO> reviews = new HashSet<>();
    private Set<ImageDTO> images = new HashSet<>();
}
