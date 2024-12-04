package com.example.isdfarmersmarket.business.services.interfaces;

import com.example.isdfarmersmarket.web.dto.*;
import org.springframework.data.domain.Pageable;

public interface GetReviewsService {

    PageResponseDTO<CustomerProductReviewDTO> fetchAllProductReviewsForCustomer(Long id, Pageable pageable);

    PageResponseDTO<CustomerFarmerReviewDTO> fetchAllFarmerReviewsForCustomer(Long id, Pageable pageable);

    PageResponseDTO<ProductReviewDTO> getProductReviews(Long id, Pageable pageable);

    PageResponseDTO<FarmerReviewDTO> getFarmerReviews(Long farmerId, Pageable pageable);

}
