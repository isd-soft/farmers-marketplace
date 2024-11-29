package com.example.isdfarmersmarket.web.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemInOrderDTO {
    private Long productId;
    private String productTitle;
    private String productDescription;
    private float quantity;
    private BigDecimal pricePerUnit;
    private String imageBase64;
    private Integer reviewCount;
    private Float rating;
}
