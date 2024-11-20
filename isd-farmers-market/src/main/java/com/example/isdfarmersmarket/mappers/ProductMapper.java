package com.example.isdfarmersmarket.mappers;

import com.example.isdfarmersmarket.DTOs.ProductDTO;
import com.example.isdfarmersmarket.models.Product;
import com.example.isdfarmersmarket.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductMapper implements Mapper<Product, ProductDTO> {
    private final ProductRepository repository;
    @Override
    public ProductDTO toDto(Product product) {
        ProductDTO productDTO = new ProductDTO(product.getId(), product.getTitle(), product.getDescription(), product.getUnitType(),
                product.getPricePerUnit(), product.getDiscountPercents(), product.getQuantity(), product.getCategory(),
                product.getUser(), product.getRating(), product.getReviews(), product.getImages());
        return productDTO;
    }

    @Override
    public Product toEntity(ProductDTO product) {
        return repository.findById(product.getId()).orElse(null);
    }
    public Product toEntityNew(ProductDTO productDTO) {
        Product product = new Product();
        product.setTitle(productDTO.getTitle());
        product.setDescription(productDTO.getDescription());
        product.setUnitType(productDTO.getUnitType());
        product.setPricePerUnit(productDTO.getPricePerUnit());
        product.setDiscountPercents(productDTO.getDiscountPercents());
        product.setQuantity(productDTO.getQuantity());
        product.setCategory(productDTO.getCategory());
        product.setUser(productDTO.getUser());
        product.setRating(productDTO.getRating());
        product.setReviews(productDTO.getReviews());
        product.setImages(productDTO.getImages());
        return product;
    }
}
