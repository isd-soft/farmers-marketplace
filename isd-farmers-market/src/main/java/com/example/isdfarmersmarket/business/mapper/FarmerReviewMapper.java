package com.example.isdfarmersmarket.business.mapper;

import com.example.isdfarmersmarket.dao.models.FarmerReview;
import com.example.isdfarmersmarket.web.dto.FarmerReviewDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserProfileMapper.class})
public interface FarmerReviewMapper {
    @Mapping(source = "farmer", target = "farmer")
    @Mapping(source = "creator", target = "creator")
    FarmerReviewDTO map(FarmerReview farmerReview);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "farmer", ignore = true)
    @Mapping(target = "creator", ignore = true)
    FarmerReview map(FarmerReviewDTO farmerReviewDto);
}