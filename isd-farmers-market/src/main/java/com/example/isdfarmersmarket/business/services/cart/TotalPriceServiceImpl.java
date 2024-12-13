package com.example.isdfarmersmarket.business.services.cart;

import com.example.isdfarmersmarket.dao.enums.DeliveryTypes;
import com.example.isdfarmersmarket.dao.models.DeliveryTypeFarmer;
import com.example.isdfarmersmarket.dao.models.ItemInCart;
import com.example.isdfarmersmarket.dao.models.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TotalPriceServiceImpl implements TotalPriceService {
    @Override
    public BigDecimal getTotalPriceOfProducts(List<ItemInCart> itemInCarts) {
        BigDecimal totalPriceOfProducts = new BigDecimal(0);
            itemInCarts.forEach(item -> {
                item.getProduct().setPricePerUnit(item.getProduct().getPricePerUnit()
                        .subtract(item.getProduct().getPricePerUnit()
                                .multiply(BigDecimal.valueOf(item.getProduct().getDiscountPercents() / 100))));
            });

            for (var item : itemInCarts) {
                totalPriceOfProducts = totalPriceOfProducts.add(item.getProduct().getPricePerUnit().multiply(BigDecimal.valueOf(item.getQuantity())));
            }
        return totalPriceOfProducts;
    }

    @Override
    public BigDecimal getTotalPriceOfDelivery(List<ItemInCart> itemInCarts, DeliveryTypes deliveryType) {
        List<User> farmers = itemInCarts.stream().map(e -> e.getProduct().getFarmer()).distinct().toList();
        BigDecimal totalPriceOfDelivery = BigDecimal.ZERO;
        for (var farmer : farmers) {
            Optional<DeliveryTypeFarmer> deliveryTypeFarmer = farmer.getDeliveryTypes()
                    .stream()
                    .filter(e -> e.getType() == deliveryType)
                    .findFirst();
            if (deliveryTypeFarmer.isPresent()) {
                totalPriceOfDelivery = totalPriceOfDelivery.add(deliveryTypeFarmer.get().getPrice());
            }
        }
        return totalPriceOfDelivery;
    }
}
