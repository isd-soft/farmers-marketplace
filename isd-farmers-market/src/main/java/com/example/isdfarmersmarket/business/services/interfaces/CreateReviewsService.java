package com.example.isdfarmersmarket.business.services.interfaces;

import com.example.isdfarmersmarket.web.commands.FarmerReviewCommand;
import com.example.isdfarmersmarket.web.commands.ProductReviewCommand;
import com.example.isdfarmersmarket.web.dto.*;

public interface CreateReviewsService {

    FarmerReviewDTO rateFarmer(FarmerReviewCommand farmerReviewCommand);

    ProductReviewDTO rateProduct(ProductReviewCommand productReviewCommand);

}
