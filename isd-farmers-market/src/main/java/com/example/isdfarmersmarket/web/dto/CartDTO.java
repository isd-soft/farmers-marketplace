package com.example.isdfarmersmarket.web.dto;

import com.example.isdfarmersmarket.dao.enums.DeliveryTypes;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartDTO {
    List<ItemInCartDTO> itemInCartDTOS;
    BigDecimal totalPriceOfProducts;
    BigDecimal totalPriceOfDelivery;
    BigDecimal totalPrice;
    DeliveryTypes deliveryTypeFarmer;
}
