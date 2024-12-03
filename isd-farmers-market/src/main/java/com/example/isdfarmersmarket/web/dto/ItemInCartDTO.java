package com.example.isdfarmersmarket.web.dto;

import com.example.isdfarmersmarket.dao.models.Product;
import com.example.isdfarmersmarket.dao.models.User;
import jakarta.persistence.*;
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
public class ItemInCartDTO {
    Long productId;
    String productTitle;
    String productDescription;
    int quantity;
    BigDecimal pricePerUnit;
    String imageBase64;

}
