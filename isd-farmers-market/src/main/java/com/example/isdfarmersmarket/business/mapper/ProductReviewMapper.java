package com.example.isdfarmersmarket.business.mapper;

import com.example.isdfarmersmarket.dao.models.ProductReview;
import com.example.isdfarmersmarket.web.dto.ProductReviewDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductReviewMapper {
    ProductReviewDTO map(ProductReview productReview);
    List<ProductReviewDTO> mapProductReviews(List<ProductReview> productReviews);
}
