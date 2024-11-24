package com.example.isdfarmersmarket.business.services;


import com.example.isdfarmersmarket.dao.models.Product;
import com.example.isdfarmersmarket.web.commands.CreateProductCommand;
import com.example.isdfarmersmarket.web.commands.UpdateProductCommand;
import com.example.isdfarmersmarket.web.dto.ProductDTO;
import com.example.isdfarmersmarket.web.dto.ProductReviewDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface ProductService {
    ProductDTO createProduct(CreateProductCommand createProductCommand, Set<MultipartFile> files);

    ProductDTO updateProduct(Long id, UpdateProductCommand updateProductCommand, Set<MultipartFile> files, Set<Long> imagesToDeleteId);

    ProductDTO deleteProduct(Long id);

    List<ProductDTO> getAllProducts();
    ProductDTO getProductById(Long id);
    List<ProductReviewDTO> getProductReviews(Long id);
    void updateProductReview(Product product);
}
