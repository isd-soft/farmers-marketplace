package com.example.isdfarmersmarket.web.dto;

import com.example.isdfarmersmarket.dao.enums.DeliveryTypes;
import com.example.isdfarmersmarket.dao.models.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class DeliveryTypeDTO {
    private Long id;
    private BigDecimal price;
    private DeliveryTypes type;
    private User farmer;
}
