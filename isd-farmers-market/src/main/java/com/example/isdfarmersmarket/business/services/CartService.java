package com.example.isdfarmersmarket.business.services;

import com.example.isdfarmersmarket.web.commands.ItemInCartCommand;
import com.example.isdfarmersmarket.web.dto.ItemInCartDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CartService {
    void addToCard(ItemInCartCommand item);
    void removeFromCard(Long id);
    List<ItemInCartDTO> getAllCartItems();
}
