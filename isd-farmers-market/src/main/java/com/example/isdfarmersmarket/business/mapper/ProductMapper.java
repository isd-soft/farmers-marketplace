package com.example.isdfarmersmarket.business.mapper;

import com.example.isdfarmersmarket.dao.models.Product;
import com.example.isdfarmersmarket.web.dto.ProductDTO;
import com.example.isdfarmersmarket.web.dto.CompactProductDTO;
import com.example.isdfarmersmarket.web.dto.ProductPageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = ImageMapper.class)
public interface ProductMapper {
    ProductDTO map(Product product);

    @Mapping(target = "image", expression = "java(imageMapper.map(product.getImages().stream().findFirst().orElse(null)))")
    @Mapping(target = "unitTypeShort", expression = "java(product.getUnitType().getShortName())")
    CompactProductDTO mapToProductInWishlistDTO(Product product);
    @Mapping(target = "image", expression = "java(imageMapper.map(product.getImages().stream().findFirst().orElse(null)))")
    @Mapping(target = "unitTypeShort", expression = "java(product.getUnitType().getShortName())")
    List<CompactProductDTO> mapToCompactProductsDTO(List<Product> product);

    ProductPageDTO mapToProductPage(Product product);
    @Mapping(target = "unitTypeShort", expression = "java(product.getUnitType().getShortName())")
    List<ProductDTO> mapProducts(List<Product> products);


}
