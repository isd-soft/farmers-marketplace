package com.example.isdfarmersmarket.web.commands;

import com.example.isdfarmersmarket.dao.enums.DeliveryTypes;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateDeliveryTypeCommand {
    private BigDecimal price;
    private DeliveryTypes types;
}
