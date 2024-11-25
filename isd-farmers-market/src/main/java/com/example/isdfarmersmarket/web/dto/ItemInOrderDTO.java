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
    private Long id;
    private Long productId;
    private float quantity;
    private BigDecimal pricePerUnit;
    private Long orderId;
}
