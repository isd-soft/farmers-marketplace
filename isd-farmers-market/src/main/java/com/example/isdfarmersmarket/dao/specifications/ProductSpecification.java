package com.example.isdfarmersmarket.dao.specifications;

import com.example.isdfarmersmarket.dao.models.Product;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor
public class ProductSpecification {
    public static Specification<Product> titleOrDescLike(String titleOrDescLike) {
        return (root, query, builder) -> builder.or(
                builder.like(builder.lower(root.get("title")), "%" + titleOrDescLike.toLowerCase() + "%"),
                builder.like(builder.lower(root.get("description")), "%" + titleOrDescLike.toLowerCase() + "%"));
    }
    public static Specification<Product> categoryIs(Long categoryId) {
        return (root, query, builder) ->
            builder.equal(root.get("category").get("id"), categoryId);
    }
    public static Specification<Product> creatorIs(Long userId) {
        return (root, query, builder) ->
                builder.equal(root.get("farmer").get("id"), userId);
    }

}
