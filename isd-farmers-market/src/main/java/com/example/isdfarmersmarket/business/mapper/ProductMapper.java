package com.example.isdfarmersmarket.business.mapper;

import com.example.isdfarmersmarket.dao.models.Product;
import com.example.isdfarmersmarket.web.dto.ProductDTO;
import com.example.isdfarmersmarket.web.dto.ProductInWishlistDTO;
import com.example.isdfarmersmarket.web.dto.ProductPageDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO map(Product product);
    ProductInWishlistDTO mapToProductInWishlistDTO(Product product);
    ProductPageDTO mapToProductPage(Product product);
    List<ProductDTO> mapProducts(List<Product> products);
}
