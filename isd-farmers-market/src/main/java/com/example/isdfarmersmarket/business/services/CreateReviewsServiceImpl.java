package com.example.isdfarmersmarket.business.services;

import com.example.isdfarmersmarket.business.mapper.ReviewMapper;
import com.example.isdfarmersmarket.business.security.JwtPrincipal;
import com.example.isdfarmersmarket.business.services.interfaces.CreateReviewsService;
import com.example.isdfarmersmarket.business.utils.SecurityUtils;
import com.example.isdfarmersmarket.dao.enums.ERole;
import com.example.isdfarmersmarket.dao.models.*;
import com.example.isdfarmersmarket.dao.repositories.*;
import com.example.isdfarmersmarket.web.commands.FarmerReviewCommand;
import com.example.isdfarmersmarket.web.commands.ProductReviewCommand;
import com.example.isdfarmersmarket.web.dto.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CreateReviewsServiceImpl implements CreateReviewsService {

    UserRepository userRepository;
    ProductRepository productRepository;
    FarmerReviewRepository farmerReviewRepository;
    ProductReviewRepository productReviewRepository;
    OrderRepository orderRepository;
    ReviewMapper reviewMapper;

    @Transactional
    public FarmerReviewDTO rateFarmer(FarmerReviewCommand farmerReviewCommand) {
        JwtPrincipal principal = SecurityUtils.getPrincipal();
        User creator = userRepository.findById(principal.getId())
                .orElseThrow(EntityNotFoundException::new);

        User farmer = userRepository.findById(farmerReviewCommand.getFarmerId())
                .orElseThrow(EntityNotFoundException::new);

        if(!canReviewFarmer(creator, farmer)) {
            throw new AccessDeniedException("You don't have permission to review farmer");
        }

        FarmerReview review = reviewMapper.map(farmerReviewCommand);
        review.setCreator(creator);
        review.setFarmer(farmer);
        farmerReviewRepository.save(review);

        updateFarmerRating(farmer);
        return reviewMapper.map(review);
    }

    @Transactional
    public ProductReviewDTO rateProduct(ProductReviewCommand productReviewCommand) {
        JwtPrincipal principal = SecurityUtils.getPrincipal();
        User creator = userRepository.findById(principal.getId())
                .orElseThrow(() -> new EntityNotFoundException("No such customer found"));

        Product product = productRepository.findById(productReviewCommand.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        if(!canReviewProduct(creator, product)) {
            throw new AccessDeniedException("You don't have permission to review product");
        }

        ProductReview review = reviewMapper.map(productReviewCommand);
        review.setCreator(creator);
        review.setProduct(product);

        productReviewRepository.save(review);
        updateProductRating(product);
        return reviewMapper.mapWithProductDetails(review);
    }
    public boolean canReviewFarmer(User creator, User farmer) {
        boolean hasAlreadyReviewed = farmerReviewRepository.existsByCreatorAndFarmer(creator, farmer);
        boolean hasCompletedOrder = orderRepository.hasCustomerCompletedOrderWithFarmer(creator, farmer);

        return !hasAlreadyReviewed && hasCompletedOrder;
    }

    public boolean canReviewProduct(User creator, Product product){
        boolean hasAlreadyReviewed = productReviewRepository.existsByCreatorAndProduct(creator, product);
        boolean hasCompletedOrder = orderRepository.hasCustomerCompletedOrderWithProduct(creator, product);

        return !hasAlreadyReviewed && hasCompletedOrder;
    }


    protected void updateProductRating(Product product) {
        ReviewStatsDTO reviewStatsDTO = productReviewRepository
                .findReviewStatsByProduct(product);

        product.setRating(reviewStatsDTO
                .getAverageRating()
                .floatValue());
        product.setReviewCount(reviewStatsDTO
                .getReviewCount()
                .intValue());

        productRepository.save(product);
    }

    private void updateFarmerRating(User farmer){
        ReviewStatsDTO reviewStatsDTO = farmerReviewRepository
                .findReviewStatsByFarmer(farmer);

        farmer.setRating(reviewStatsDTO.getAverageRating().floatValue());
        farmer.setReviewCount(reviewStatsDTO.getReviewCount().intValue());
    }

}
