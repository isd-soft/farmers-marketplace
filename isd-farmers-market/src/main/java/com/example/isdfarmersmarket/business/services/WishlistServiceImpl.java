package com.example.isdfarmersmarket.business.services;

import com.example.isdfarmersmarket.business.mapper.ProductMapper;
import com.example.isdfarmersmarket.business.security.JwtPrincipal;
import com.example.isdfarmersmarket.business.services.interfaces.WishlistService;
import com.example.isdfarmersmarket.business.utils.SecurityUtils;
import com.example.isdfarmersmarket.dao.models.Product;
import com.example.isdfarmersmarket.dao.models.User;
import com.example.isdfarmersmarket.dao.repositories.ProductRepository;
import com.example.isdfarmersmarket.dao.repositories.UserRepository;
import com.example.isdfarmersmarket.web.dto.CompactProductDTO;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WishlistServiceImpl implements WishlistService {

    UserRepository userRepository;
    ProductRepository productRepository;
    ProductMapper productMapper;

    @Transactional(readOnly = true)
    public List<CompactProductDTO> getWishlistProducts() {
        JwtPrincipal principal = SecurityUtils.getPrincipal();
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new EntityNotFoundException("No such user found"));
        Set<Product> wishlist = user.getWishlist();

        return productMapper.mapToCompactProductsDTO( user.getWishlist().stream().toList(), wishlist);
    }

    @Transactional
    public CompactProductDTO addProductToWishlist(Long productId) {
        JwtPrincipal principal = SecurityUtils.getPrincipal();
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new EntityNotFoundException("No such user found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        if (user.getWishlist().contains(product)) {
            throw new EntityExistsException("Product already in wishlist");
        }

        user.getWishlist().add(product);
        userRepository.save(user);
        return productMapper.mapToProductInWishlistDTO(product);
    }

    @Transactional
    public CompactProductDTO deleteProductFromWishlist(Long productId) {
        JwtPrincipal principal = SecurityUtils.getPrincipal();
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new EntityNotFoundException("No such user found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        if (!user.getWishlist().contains(product)) {
            throw new EntityExistsException("Product not in wishlist");
        }

        user.getWishlist().remove(product);
        userRepository.save(user);
        return productMapper.mapToProductInWishlistDTO(product);
    }
}
