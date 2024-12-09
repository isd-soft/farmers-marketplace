package com.example.isdfarmersmarket.web.dto;

import com.example.isdfarmersmarket.dao.enums.UnitType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemInOrderDTO {
    Long productId;
    String productTitle;
    String productDescription;
    int quantity;
    BigDecimal pricePerUnit;
    String imageBase64;
    Integer reviewCount;
    Float rating;
    UnitType unitType;
    String unitTypeShort;
    Boolean isInWishlist;
}
