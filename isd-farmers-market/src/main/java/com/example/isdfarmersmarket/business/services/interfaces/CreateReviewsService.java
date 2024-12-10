package com.example.isdfarmersmarket.business.services.interfaces;

import com.example.isdfarmersmarket.dao.models.Product;
import com.example.isdfarmersmarket.dao.models.User;
import com.example.isdfarmersmarket.web.commands.FarmerReviewCommand;
import com.example.isdfarmersmarket.web.commands.ProductReviewCommand;
import com.example.isdfarmersmarket.web.dto.*;

public interface CreateReviewsService {

    FarmerReviewDTO rateFarmer(FarmerReviewCommand farmerReviewCommand);

    ProductReviewDTO rateProduct(ProductReviewCommand productReviewCommand);

    boolean canReviewFarmer(User creator, User farmer);
    boolean canReviewProduct(User creator, Product product);

}
