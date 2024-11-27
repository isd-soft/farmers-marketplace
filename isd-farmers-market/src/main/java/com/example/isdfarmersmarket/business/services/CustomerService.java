        package com.example.isdfarmersmarket.business.services;

import com.example.isdfarmersmarket.business.mapper.ProductMapper;
import com.example.isdfarmersmarket.business.mapper.ReviewMapper;
import com.example.isdfarmersmarket.business.security.JwtPrincipal;
import com.example.isdfarmersmarket.dao.enums.ERole;
import com.example.isdfarmersmarket.dao.models.*;
import com.example.isdfarmersmarket.dao.repositories.*;
import com.example.isdfarmersmarket.web.commands.FarmerReviewCommand;
import com.example.isdfarmersmarket.web.commands.ProductReviewCommand;
import com.example.isdfarmersmarket.web.dto.*;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    ProductService productService;
    private final ProductMapper productMapper;
    private final FarmerService farmerService;

    private JwtPrincipal getPrincipal() {
        return (JwtPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Transactional
    public FarmerReviewDTO rateFarmer(FarmerReviewCommand farmerReviewCommand) {
        JwtPrincipal principal = getPrincipal();

        User creator = userRepository
                .findById(principal.getId())
                .orElseThrow(EntityNotFoundException::new);
        User farmer = userRepository
                .findById(farmerReviewCommand.getFarmerId())
                .orElseThrow(() -> new EntityNotFoundException("No such farmer found"));

        Role farmerRole = roleRepository
                .findByRole(ERole.FARMER).orElseThrow(() ->
                new EntityNotFoundException("Role doesn't exist"));

        if (farmer.getRoles().contains(farmerRole)) {
            FarmerReview review = reviewMapper.map(farmerReviewCommand);
            review.setCreator(creator);
            review.setFarmer(farmer);
            FarmerReview farmerReview = farmerReviewRepository.save(review);
            farmerService.updateFarmerRating(farmer);
            return reviewMapper.map(farmerReview);
        } else {
            throw new IllegalArgumentException("The email provided is not a farmer.");
        }
    }

    @Transactional
    public ProductReviewDTO rateProduct(ProductReviewCommand productReviewCommand) {
        JwtPrincipal principal = getPrincipal();
        User creator = userRepository
                .findById(principal.getId())
                .orElseThrow(() -> new EntityNotFoundException("No such customer found"));
        Product product = productRepository
                .findById(productReviewCommand.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        ProductReview review = reviewMapper.map(productReviewCommand);
        review.setCreator(creator);
        review.setProduct(product);
        ProductReview productReview = productReviewRepository.save(review);
        productService.updateProductRating(product);
        return reviewMapper.mapWithProductDetails(productReview);
    }

    @Transactional(readOnly = true)
    public List<CustomerProductReviewDTO> fetchAllProductReviews(Long id, Integer page, Integer pageSize) {
        User customer = userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No such user found"));

        return productReviewRepository.findByCreatorOrderByCreatedDate(customer, PageRequest.of(page, pageSize))
                .stream()
                .map(reviewMapper::mapToCustomerProductReview)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CustomerFarmerReviewDTO> fetchAllFarmerReviews(Long id, Integer page, Integer pageSize) {
        User farmer = userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No such user found"));

        return farmerReviewRepository.findByCreatorOrderByCreatedDate(farmer, PageRequest.of(page, pageSize))
                .stream()
                .map(reviewMapper::mapToCustomerFarmerReview)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CompactProductDTO> getWishlistProducts() {
        JwtPrincipal principal = (JwtPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new EntityNotFoundException("No such user found"));
        return user.getWishlist().stream()
                .map(productMapper::mapToProductInWishlistDTO)
                .toList();
    }

    @Transactional
    public CompactProductDTO addProductToWishlist(Long productId) {
        JwtPrincipal principal = getPrincipal();
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new EntityNotFoundException("No such user found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        if(user.getWishlist().contains(product)) {
            throw new EntityExistsException("Product already in wishlist");
        }
        user.getWishlist().add(product);
        userRepository.save(user);
        return productMapper.mapToProductInWishlistDTO(product);
    }
    @Transactional
    public CompactProductDTO deleteProductFromWishlist(Long productId) {
        JwtPrincipal principal = getPrincipal();
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new EntityNotFoundException("No such user found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        if(!user.getWishlist().contains(product)) {
            throw new EntityExistsException("Product doesnt exist in the wishlist");
        }
        user.getWishlist().remove(product);
        userRepository.save(user);
        return productMapper.mapToProductInWishlistDTO(product);
    }
}