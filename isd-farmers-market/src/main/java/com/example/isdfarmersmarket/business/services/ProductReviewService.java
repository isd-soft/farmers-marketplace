package com.example.isdfarmersmarket.business.services;

import com.example.isdfarmersmarket.web.commands.CreateProductReviewCommand;
import com.example.isdfarmersmarket.web.dto.ProductReviewDTO;

import java.util.List;

public interface ProductReviewService {
    ProductReviewDTO createProductReview(CreateProductReviewCommand createProductReviewCommand);
    ProductReviewDTO deleteProductReview(Long id);
    List<ProductReviewDTO> getAllProductReviews();
    ProductReviewDTO getProductReviewById(Long id);
}
