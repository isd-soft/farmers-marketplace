package com.example.isdfarmersmarket.dao.specifications;

import com.example.isdfarmersmarket.dao.models.Product;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor
public class ProductSpecification {
    public static Specification<Product> titleOrDescLike(String titleOrDescLike) {
        return (root, query, builder) -> builder.or(
                builder.like(root.get("title"), "%" + titleOrDescLike + "%"),
                builder.like(root.get("description"), "%" + titleOrDescLike + "%"));
    }

}
