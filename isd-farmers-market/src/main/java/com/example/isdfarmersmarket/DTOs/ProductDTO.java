package com.example.isdfarmersmarket.DTOs;

import com.example.isdfarmersmarket.enums.UnitType;
import com.example.isdfarmersmarket.models.Category;
import com.example.isdfarmersmarket.models.Image;
import com.example.isdfarmersmarket.models.ProductReview;
import com.example.isdfarmersmarket.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class ProductDTO {

    private Long id;
    private String title;
    private String description;
    private UnitType unitType;
    private BigDecimal pricePerUnit;
    private int discountPercents;
    private int quantity;
    private Category category;
    private User user;
    private Float rating;
    private Set<ProductReview> reviews = new HashSet<>();
    private Set<Image> images = new HashSet<>();
}
