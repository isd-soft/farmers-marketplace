package com.example.isdfarmersmarket.web.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductInfoDTO {

    private final String title;
    private final Integer quantity;
    private final BigDecimal pricePerUnit;
    private BigDecimal totalPrice;

    public ProductInfoDTO(String title, Integer quantity, BigDecimal pricePerUnit) {
        this.title = title;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.totalPrice = pricePerUnit.multiply(BigDecimal.valueOf(quantity));
    }
}
