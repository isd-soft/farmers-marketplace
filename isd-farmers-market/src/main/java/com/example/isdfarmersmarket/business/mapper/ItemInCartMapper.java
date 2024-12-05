package com.example.isdfarmersmarket.business.mapper;

import com.example.isdfarmersmarket.dao.models.Image;
import com.example.isdfarmersmarket.dao.models.ItemInCart;
import com.example.isdfarmersmarket.dao.models.ItemInOrder;
import com.example.isdfarmersmarket.web.commands.cart.AddItemInCartCommand;
import com.example.isdfarmersmarket.web.dto.ItemInCartDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Base64;
import java.util.List;
@Mapper(componentModel = "spring")
public interface ItemInCartMapper {
    ItemInCart mapToEntity(AddItemInCartCommand addItemInCartCommand);
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productTitle", source = "product.title")
    @Mapping(target = "productDescription", source = "product.description")
    @Mapping(target = "imageBase64", expression = "java(convertImageToBase64(itemInCart.getProduct().getImages().stream().findFirst().orElse(null)))")
    @Mapping(target = "pricePerUnit", source = "product.pricePerUnit")
    @Mapping(target = "totalProductQuantity", source = "product.quantity")
    @Mapping(target = "discountPercents", source = "product.discountPercents")
    @Mapping(target = "unitType", source = "product.unitType")
    ItemInCartDTO mapToDTO(ItemInCart itemInCart);
    List<ItemInCartDTO> mapToDTOs(List<ItemInCart> itemInCart);

    @Mapping(target = "product", source = "product")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "pricePerUnit", source = "product.pricePerUnit")
    ItemInOrder mapToItemInOrder(ItemInCart itemInCart);
    List<ItemInOrder> mapToItemInOrder(List<ItemInCart> itemInCartList);

    default String convertImageToBase64(Image image) {
        return image == null ? null : Base64.getEncoder().encodeToString(image.getBytes());
    }
}