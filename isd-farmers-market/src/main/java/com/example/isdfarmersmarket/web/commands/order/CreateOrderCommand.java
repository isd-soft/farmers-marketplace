package com.example.isdfarmersmarket.web.commands.order;

import com.example.isdfarmersmarket.dao.enums.DeliveryTypes;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CreateOrderCommand {
    @NotNull(message = "Delivery Type cannot be null")
    BigDecimal totalPriceOfProducts;
    BigDecimal totalPriceOfDelivery;
    BigDecimal totalPrice;
    String deliveryTypeFarmer;
}


