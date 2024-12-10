package com.example.isdfarmersmarket.business.mapper;

import com.example.isdfarmersmarket.dao.models.Product;
import com.example.isdfarmersmarket.web.dto.ProductDTO;
import com.example.isdfarmersmarket.web.dto.CompactProductDTO;
import com.example.isdfarmersmarket.web.dto.ProductPageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = ImageMapper.class)
public interface ProductMapper {
    ProductDTO map(Product product);

    @Mapping(target = "image", expression = "java(imageMapper.map(product.getImages().stream().findFirst().orElse(null)))")
    @Mapping(target = "unitTypeShort", expression = "java(product.getUnitType().getShortName())")
    CompactProductDTO mapToProductInWishlistDTO(Product product);

    default Page<CompactProductDTO> mapToCompactProductsDTO(Page<Product> products, Set<Product> wishlist) {
        return products.map(product -> {
            CompactProductDTO compactProductDTO = this.mapToProductInWishlistDTO(product);
            if (wishlist.contains(product)) {
                compactProductDTO.setIsInWishlist(true);
            } else {
                compactProductDTO.setIsInWishlist(false);
            }
            return compactProductDTO;
        });
    }
    default List<CompactProductDTO> mapToCompactProductsDTO(List<Product> products, Set<Product> wishlist) {
        return products.stream().map(product -> {
            CompactProductDTO compactProductDTO = this.mapToProductInWishlistDTO(product);
            if (wishlist.contains(product)) {
                compactProductDTO.setIsInWishlist(true);
            } else {
                compactProductDTO.setIsInWishlist(false);
            }
            return compactProductDTO;
        }).toList();
    }
    default Page<CompactProductDTO> mapToCompactProductsDTO(Page<Product> products) {
        return products.map(this::mapToProductInWishlistDTO);
    }

    ProductPageDTO mapToProductPage(Product product);
    @Mapping(target = "unitTypeShort", expression = "java(product.getUnitType().getShortName())")
    List<ProductDTO> mapProducts(List<Product> products);

}
