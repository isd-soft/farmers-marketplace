package com.example.isdfarmersmarket.business.mapper;

import com.example.isdfarmersmarket.dao.models.ItemInCart;
import com.example.isdfarmersmarket.dao.models.ItemInOrder;
import com.example.isdfarmersmarket.web.commands.ItemInCartCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemInCartMapper {
    ItemInCart map(ItemInCartCommand itemInCartCommand);
    @Mapping(target = "product", source = "product")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "pricePerUnit", source = "product.pricePerUnit")
    ItemInOrder map(ItemInCart itemInCartCommand);
    List<ItemInOrder> map(List<ItemInCart> itemInCartList);

}
