package com.example.isdfarmersmarket.dao.specifications;

import com.example.isdfarmersmarket.dao.models.Order;
import com.example.isdfarmersmarket.dao.models.Product;
import org.springframework.data.jpa.domain.Specification;

public class OrderSpecification {
//    public static Specification<Order> titleOrDescLike(String titleOrDescLike) {
//        return (root, query, builder) -> builder.or(
//                builder.like(builder.lower(root.get("title")), "%" + titleOrDescLike.toLowerCase() + "%"),
//                builder.like(builder.lower(root.get("description")), "%" + titleOrDescLike.toLowerCase() + "%"));
//    }
    public static Specification<Order> orderIs(String orderStatus) {
        return (root, query, builder) ->
                builder.equal(root.get("orderStatus"), orderStatus);
    }

    public static Specification<Order> customerIs(Long userId) {
        return (root, query, builder) ->
                builder.equal(root.get("customer").get("id"), userId);
    }
}
