package com.example.isdfarmersmarket.business.services;

import com.example.isdfarmersmarket.web.commands.ItemInCartCommand;

public interface CartService {
    void addToCard(ItemInCartCommand item);
    void removeFromCard(Long id);
}
