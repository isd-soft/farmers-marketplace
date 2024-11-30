package com.example.isdfarmersmarket.business.services;


import com.example.isdfarmersmarket.business.security.JwtPrincipal;
import com.example.isdfarmersmarket.dao.models.Product;
import com.example.isdfarmersmarket.web.commands.CreateProductCommand;
import com.example.isdfarmersmarket.web.commands.UpdateProductCommand;
import com.example.isdfarmersmarket.web.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ProductService {
    ProductDTO createProduct(CreateProductCommand createProductCommand);

    ProductDTO updateProduct(Long id, UpdateProductCommand updateProductCommand);

    ProductDTO deleteProduct(Long id);
    Map<String, Object> getAllProducts(Long category, String search, Pageable pageable);
    Map<String, Object> getCurrentUserProducts(Pageable pageable);
    ProductDTO getProductById(Long id);

    ProductPageDTO getProductPageById(Long id);
}
