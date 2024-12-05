package com.example.isdfarmersmarket.business.services.interfaces;

import com.example.isdfarmersmarket.web.commands.ItemInCartCommand;
import com.example.isdfarmersmarket.web.dto.ItemInCartDTO;

import java.util.List;

public interface CartService {

    ItemInCartDTO addToCart(ItemInCartCommand item);

    ItemInCartDTO removeFromCart(Long id);

    List<ItemInCartDTO> getAllCartItems();
}
