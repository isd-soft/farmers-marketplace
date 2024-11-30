package com.example.isdfarmersmarket.business.mapper;

import com.example.isdfarmersmarket.dao.models.ItemInCart;
import com.example.isdfarmersmarket.dao.models.ItemInOrder;
import com.example.isdfarmersmarket.web.commands.ItemInCartCommand;
import com.example.isdfarmersmarket.web.dto.ItemInCartDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemInCartMapper {
    ItemInCart mapToEntity(ItemInCartCommand itemInCartCommand);
    @Mapping(target = "productId", source = "product.id")
    ItemInCartDTO mapToDTO(ItemInCart itemInCart);
    List<ItemInCartDTO> mapToDTO(List<ItemInCart> itemInCart);

    @Mapping(target = "product", source = "product")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "pricePerUnit", source = "product.pricePerUnit")
    ItemInOrder mapToItemInOrder(ItemInCart itemInCart);
    List<ItemInOrder> mapToItemInOrder(List<ItemInCart> itemInCartList);

}
