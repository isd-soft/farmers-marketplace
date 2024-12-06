package com.example.isdfarmersmarket.web.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartDTO {
    private List<ItemInCartDTO> itemInCartDTOS;
    private BigDecimal totalPriceOfProducts;
    private BigDecimal totalPriceOfDelivery;
    private BigDecimal totalPrice;
}
