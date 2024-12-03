package com.example.isdfarmersmarket.business.mapper;

import com.example.isdfarmersmarket.dao.models.FarmerReview;
import com.example.isdfarmersmarket.dao.models.ProductReview;
import com.example.isdfarmersmarket.web.commands.FarmerReviewCommand;
import com.example.isdfarmersmarket.web.commands.ProductReviewCommand;
import com.example.isdfarmersmarket.web.dto.CustomerFarmerReviewDTO;
import com.example.isdfarmersmarket.web.dto.CustomerProductReviewDTO;
import com.example.isdfarmersmarket.web.dto.FarmerReviewDTO;
import com.example.isdfarmersmarket.web.dto.ProductReviewDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = {UserProfileMapper.class, ProductMapper.class})
public interface ReviewMapper {

    @Mapping(source = "creator", target = "creator")
    @Mapping(source = "product.title", target = "productTitle")
    @Mapping(source = "product.id", target = "productId")
    ProductReviewDTO mapWithProductDetails(ProductReview productReview);

    @Mapping(source = "creator", target = "creator")
    ProductReviewDTO mapWithoutProductDetails(ProductReview productReview);

    @Mapping(source = "product", target = "product")
    CustomerProductReviewDTO mapToCustomerProductReview(ProductReview productReview);
    @Mapping(source = "farmer", target = "farmer")
    CustomerFarmerReviewDTO mapToCustomerFarmerReview(FarmerReview productReview);

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
