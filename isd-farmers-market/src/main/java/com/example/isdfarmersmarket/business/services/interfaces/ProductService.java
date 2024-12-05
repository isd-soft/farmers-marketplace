package com.example.isdfarmersmarket.business.services.interfaces;


import com.example.isdfarmersmarket.web.commands.CreateProductCommand;
import com.example.isdfarmersmarket.web.commands.UpdateProductCommand;
import com.example.isdfarmersmarket.web.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductDTO createProduct(CreateProductCommand createProductCommand);

    ProductDTO updateProduct(Long id, UpdateProductCommand updateProductCommand);
    ProductDTO setDiscountProduct(Long id, int discount);

    ProductDTO deleteProduct(Long id);
    Page<CompactProductDTO> getAllProducts(Long category, String search, Pageable pageable);
    Page<CompactProductDTO> getCurrentUserProducts(Pageable pageable);
    ProductDTO getProductById(Long id);

    ProductPageDTO getProductPageById(Long id);
}
