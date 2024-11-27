package com.example.isdfarmersmarket.business.mapper;

import com.example.isdfarmersmarket.dao.models.Image;
import com.example.isdfarmersmarket.dao.models.Product;
import com.example.isdfarmersmarket.web.dto.ImageDTO;
import com.example.isdfarmersmarket.web.dto.ProductDTO;
import com.example.isdfarmersmarket.web.dto.ProductInWishlistDTO;
import com.example.isdfarmersmarket.web.dto.ProductPageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = ImageMapper.class)
public interface ProductMapper {
    ProductDTO map(Product product);

    @Mapping(target = "image", expression = "java(imageMapper.map(product.getImages().stream().findFirst().orElse(null)))")
    ProductInWishlistDTO mapToProductInWishlistDTO(Product product);

    ProductPageDTO mapToProductPage(Product product);
    List<ProductDTO> mapProducts(List<Product> products);


}
