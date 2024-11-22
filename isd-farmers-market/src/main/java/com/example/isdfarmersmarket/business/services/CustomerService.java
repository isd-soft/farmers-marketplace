        package com.example.isdfarmersmarket.business.services;

import com.example.isdfarmersmarket.business.mapper.ReviewMapper;
import com.example.isdfarmersmarket.dao.enums.ERole;
import com.example.isdfarmersmarket.dao.models.*;
import com.example.isdfarmersmarket.dao.repositories.*;
import com.example.isdfarmersmarket.web.commands.FarmerReviewCommand;
import com.example.isdfarmersmarket.web.commands.ProductReviewCommand;
import com.example.isdfarmersmarket.web.dto.FarmerReviewDTO;
import com.example.isdfarmersmarket.web.dto.ProductReviewDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerService {

    UserRepository userRepository;
    ProductRepository productRepository;
    ProductReviewRepository productReviewRepository;
    FarmerReviewRepository farmerReviewRepository;
    RoleRepository roleRepository;
    ReviewMapper reviewMapper;

    public FarmerReviewDTO rateFarmer(FarmerReviewCommand farmerReviewCommand,
                                      String email) {
        User creator = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("No such customer found"));
        User farmer = userRepository.findById(farmerReviewCommand.getFarmerId())
                .orElseThrow(() -> new EntityNotFoundException("No such farmer found"));

        Role farmerRole = roleRepository.findByRole(ERole.FARMER).orElseThrow(() ->
                new EntityNotFoundException("Role doesn't exist"));

        if (farmer.getRoles().contains(farmerRole)) {
            FarmerReview review = reviewMapper.map(farmerReviewCommand);
            review.setCreator(creator);
            review.setFarmer(farmer);
            FarmerReview farmerReview = farmerReviewRepository.save(review);
            return reviewMapper.map(farmerReview);
        } else {
            throw new IllegalArgumentException("The email provided is not a farmer.");
        }
    }

    public ProductReviewDTO rateProduct(ProductReviewCommand productReviewCommand, String email) {
        User creator = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("No such customer found"));
        Product product = productRepository.findById(productReviewCommand.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        ProductReview review = reviewMapper.map(productReviewCommand);
        review.setCreator(creator);
        review.setProduct(product);
        ProductReview productReview = productReviewRepository.save(review);
        return reviewMapper.map(productReview);
    }

    public List<ProductReviewDTO> fetchAllProductReviews(Long id, Integer page, Integer pageSize) {
        User customer = userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No such user found"));

        return productReviewRepository.findByCreatorOrderByCreatedDate(customer, PageRequest.of(page, pageSize))
                .stream()
                .map(reviewMapper::map)
                .toList();
    }

    public List<FarmerReviewDTO> fetchAllFarmerReviews(Long id, Integer page, Integer pageSize) {
        User farmer = userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No such user found"));

        return farmerReviewRepository.findByCreatorOrderByCreatedDate(farmer, PageRequest.of(page, pageSize))
                .stream()
                .map(reviewMapper::map)
                .toList();
    }

}