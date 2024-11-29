package com.example.isdfarmersmarket.web.dto;

import com.example.isdfarmersmarket.dao.enums.UnitType;
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
    private int quantity;
    private UnitType unitType;
    private String unitTypeShort;
    private int discountPercents;
    private Float rating;
    private ImageDTO image;
}
