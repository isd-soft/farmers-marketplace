package com.example.isdfarmersmarket.business.mapper;

import com.example.isdfarmersmarket.dao.models.ItemInCart;
import com.example.isdfarmersmarket.web.commands.ItemInCartCommand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemInCartMapper {
    ItemInCart map(ItemInCartCommand itemInCartCommand);
}
