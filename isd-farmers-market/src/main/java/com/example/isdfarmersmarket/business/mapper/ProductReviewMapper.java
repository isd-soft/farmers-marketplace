package com.example.isdfarmersmarket.business.mapper;

import com.example.isdfarmersmarket.dao.models.ProductReview;
import com.example.isdfarmersmarket.web.dto.ProductReviewDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserProfileMapper.class})
public interface ProductReviewMapper {
    @Mapping(source = "product.title", target = "productName")
    @Mapping(source = "product.id", target = "productId")
    ProductReviewDTO map(ProductReview productReview);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "creator", ignore = true)
    ProductReview map(ProductReviewDTO productReviewDTO);
}