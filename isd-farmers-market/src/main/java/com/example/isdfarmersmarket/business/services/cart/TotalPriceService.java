package com.example.isdfarmersmarket.business.services.cart;

import com.example.isdfarmersmarket.dao.enums.DeliveryTypes;
import com.example.isdfarmersmarket.dao.models.ItemInCart;

import java.math.BigDecimal;
import java.util.List;

public interface TotalPriceService {
    BigDecimal getTotalPriceOfProducts(List<ItemInCart> itemInCarts);
    BigDecimal getTotalPriceOfDelivery(List<ItemInCart> itemInCarts, DeliveryTypes deliveryType);
}
