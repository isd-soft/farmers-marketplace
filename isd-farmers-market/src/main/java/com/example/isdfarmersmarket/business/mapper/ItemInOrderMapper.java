package com.example.isdfarmersmarket.business.mapper;

import com.example.isdfarmersmarket.dao.models.Image;
import com.example.isdfarmersmarket.dao.models.ItemInOrder;
import com.example.isdfarmersmarket.dao.models.Product;
import com.example.isdfarmersmarket.web.dto.CompactProductDTO;
import com.example.isdfarmersmarket.web.dto.ItemInOrderDTO;
import com.example.isdfarmersmarket.business.utils.ImageUtilsConvertToBase64;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = ImageUtilsConvertToBase64.class)
public interface ItemInOrderMapper {
    @Mapping(target = "productId",  expression = "java(itemInOrder.getProduct().getId())")
    @Mapping(target = "productTitle",  expression = "java(itemInOrder.getProduct().getTitle())")
    @Mapping(target = "productDescription", expression = "java(itemInOrder.getProduct().getDescription())")
    @Mapping(target = "imageBase64", expression = "java(convertImageToBase64(itemInOrder.getProduct().getImages().stream().findFirst().orElse(null)))")
    @Mapping(target = "reviewCount", expression = "java(itemInOrder.getProduct().getReviews().size())")
    @Mapping(target = "rating", expression = "java(itemInOrder.getProduct().getRating())")
    @Mapping(target = "unitType", expression = "java(itemInOrder.getProduct().getUnitType())")
    @Mapping(target = "unitTypeShort", expression = "java(itemInOrder.getProduct().getUnitType().getShortName())")
    ItemInOrderDTO map(ItemInOrder itemInOrder);
    List<ItemInOrderDTO> mapOrders(List<ItemInOrder> itemInOrder);
    @Mapping(target = "productId",  expression = "java(itemInOrder.getProduct().getId())")
    @Mapping(target = "productTitle",  expression = "java(itemInOrder.getProduct().getTitle())")
    @Mapping(target = "productDescription", expression = "java(itemInOrder.getProduct().getDescription())")
    @Mapping(target = "imageBase64", expression = "java(convertImageToBase64(itemInOrder.getProduct().getImages().stream().findFirst().orElse(null)))")
    @Mapping(target = "reviewCount", expression = "java(itemInOrder.getProduct().getReviews().size())")
    @Mapping(target = "rating", expression = "java(itemInOrder.getProduct().getRating())")
    @Mapping(target = "unitTypeShort", expression = "java(itemInOrder.getProduct().getUnitType().getShortName())")
    Set<ItemInOrderDTO> mapOrders(Set<ItemInOrder> itemInOrder);

    default Set<ItemInOrderDTO> mapToItemsInOrderDTO(Set<ItemInOrder> items, Set<Product> wishlist) {
        return items.stream().map(itemInOrder -> {
            ItemInOrderDTO itemInOrderDTO = map(itemInOrder);
            if(wishlist.contains(itemInOrder.getProduct())) {
                itemInOrderDTO.setIsInWishlist(true);
            } else {
                itemInOrderDTO.setIsInWishlist(false);
            }
            return itemInOrderDTO;
        }).collect(Collectors.toSet());
    }
    default String convertImageToBase64(Image image) {
        return image == null ? null : Base64.getEncoder().encodeToString(image.getBytes());
    }
}
