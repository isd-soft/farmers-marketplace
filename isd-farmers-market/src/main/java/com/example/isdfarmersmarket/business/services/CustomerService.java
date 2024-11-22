package com.example.isdfarmersmarket.business.services;

import com.example.isdfarmersmarket.business.mapper.FarmerReviewMapper;
import com.example.isdfarmersmarket.business.mapper.ProductReviewMapper;
import com.example.isdfarmersmarket.dao.enums.ERole;
import com.example.isdfarmersmarket.dao.models.*;
import com.example.isdfarmersmarket.dao.repositories.*;
import com.example.isdfarmersmarket.web.dto.FarmerReviewDTO;
import com.example.isdfarmersmarket.web.dto.ProductReviewDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductReviewRepository productReviewRepository;
    private final FarmerReviewRepository farmerReviewRepository;
    private final RoleRepository roleRepository;
    private final ProductReviewMapper productReviewMapper;
    private final FarmerReviewMapper farmerReviewMapper;

    public void rateFarmer(FarmerReviewDTO farmerReviewDto) {
        User creator = userRepository.findByEmail(farmerReviewDto.getCreator().getEmail())
                .orElseThrow(() -> new IllegalArgumentException("No such customer found"));
        User farmer = userRepository.findByEmail(farmerReviewDto.getFarmer().getEmail())
                .orElseThrow(() -> new IllegalArgumentException("No such farmer found"));

        Role farmerRole = roleRepository.findByRole(ERole.FARMER).orElseThrow(() ->
                new EntityNotFoundException("Role doesn't exist"));

        if (farmer.getRoles().contains(farmerRole)) {
            FarmerReview review = farmerReviewMapper.map(farmerReviewDto);
            review.setCreator(creator);
            review.setFarmer(farmer);
            farmerReviewRepository.save(review);
        } else {
            throw new IllegalArgumentException("The email provided is not a farmer.");
        }
    }

    public void rateProduct(ProductReviewDTO productReviewDTO) {
        User creator = userRepository.findByEmail(productReviewDTO.getCreator().getEmail())
                .orElseThrow(() -> new IllegalArgumentException("No such customer found"));
        Product product = productRepository.findById(productReviewDTO.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        ProductReview review = productReviewMapper.map(productReviewDTO);
        review.setCreator(creator);
        review.setProduct(product);
        productReviewRepository.save(review);
    }

    public List<ProductReviewDTO> fetchAllProductReviews(String email, Integer page, Integer pageSize) {
        User customer = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No such user found"));

        return productReviewRepository.findByCreator(customer, PageRequest.of(page, pageSize))
                .stream()
                .map(productReviewMapper::map)
                .toList();
    }

    public List<FarmerReviewDTO> fetchAllFarmerReviews(String email, Integer page, Integer pageSize) {
        User farmer = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No such user found"));

        return farmerReviewRepository.findByCreator(farmer, PageRequest.of(page, pageSize))
                .stream()
                .map(farmerReviewMapper::map)
                .toList();
    }
}