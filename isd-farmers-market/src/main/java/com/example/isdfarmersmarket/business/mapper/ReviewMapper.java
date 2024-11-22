package com.example.isdfarmersmarket.business.mapper;

import com.example.isdfarmersmarket.dao.models.FarmerReview;
import com.example.isdfarmersmarket.dao.models.ProductReview;
import com.example.isdfarmersmarket.web.commands.FarmerReviewCommand;
import com.example.isdfarmersmarket.web.commands.ProductReviewCommand;
import com.example.isdfarmersmarket.web.dto.FarmerReviewDTO;
import com.example.isdfarmersmarket.web.dto.ProductReviewDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(source = "creator", target = "creator")
    @Mapping(source = "product", target = "product")
    ProductReviewDTO map(ProductReview productReview);

    @Mapping(source = "rating", target = "rating")
    @Mapping(source = "content", target = "content")
    ProductReview map(ProductReviewCommand productReviewCommand);

    @Mapping(source = "creator", target = "creator")
    @Mapping(source = "farmer", target = "farmer")
    FarmerReviewDTO map(FarmerReview farmerReview);

    @Mapping(source = "rating", target = "rating")
    @Mapping(source = "content", target = "content")
    FarmerReview map(FarmerReviewCommand farmerReviewCommand);
}
