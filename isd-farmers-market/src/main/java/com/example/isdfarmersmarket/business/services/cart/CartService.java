package com.example.isdfarmersmarket.business.services.cart;

import com.example.isdfarmersmarket.dao.enums.DeliveryTypes;
import com.example.isdfarmersmarket.web.commands.cart.AddItemInCartCommand;
import com.example.isdfarmersmarket.web.commands.cart.UpdateItemInCartCommand;
import com.example.isdfarmersmarket.web.dto.CartDTO;
import com.example.isdfarmersmarket.web.dto.ItemInCartDTO;

import java.util.List;

public interface CartService {

    ItemInCartDTO addToCart(AddItemInCartCommand item);

    ItemInCartDTO removeFromCart(Long id);
    ItemInCartDTO updateCart(Long id, UpdateItemInCartCommand updateItemInCartCommand);

    CartDTO getAllCartItems(DeliveryTypes deliveryTypes);
}
