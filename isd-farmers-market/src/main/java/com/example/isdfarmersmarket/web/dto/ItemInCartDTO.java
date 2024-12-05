package com.example.isdfarmersmarket.web.dto;

import com.example.isdfarmersmarket.dao.models.Product;
import com.example.isdfarmersmarket.dao.models.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemInCartDTO {
    Long id;
    Long productId;
    String productTitle;
    String productDescription;
    String imageBase64;
    BigDecimal pricePerUnit;
    int totalProductQuantity;
    BigDecimal totalPrice;
    int discountPercents;
    String unitType;
    int quantity;
}
