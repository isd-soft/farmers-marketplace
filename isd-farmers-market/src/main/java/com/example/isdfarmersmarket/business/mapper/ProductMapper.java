package com.example.isdfarmersmarket.business.mapper;

import com.example.isdfarmersmarket.dao.models.Product;
import com.example.isdfarmersmarket.web.dto.ProductDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO map(Product product);
    List<ProductDTO> mapProducts(List<Product> products);
}
