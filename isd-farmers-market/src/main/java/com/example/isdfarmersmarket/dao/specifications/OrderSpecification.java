package com.example.isdfarmersmarket.dao.specifications;

import com.example.isdfarmersmarket.dao.enums.OrderStatus;
import com.example.isdfarmersmarket.dao.models.Order;
import com.example.isdfarmersmarket.dao.models.Product;
import org.springframework.data.jpa.domain.Specification;

public class OrderSpecification {
//    public static Specification<Order> titleOrDescLike(String titleOrDescLike) {
//        return (root, query, builder) -> builder.or(
//                builder.like(builder.lower(root.get("title")), "%" + titleOrDescLike.toLowerCase() + "%"),
//                builder.like(builder.lower(root.get("description")), "%" + titleOrDescLike.toLowerCase() + "%"));
//    }
    public static Specification<Order> customerIs(Long userId) {
        return (root, query, builder) ->
                builder.equal(root.get("customer").get("id"), userId);
    }
    public static Specification<Order> statusIs(String status) {
        return (root, query, builder) -> status == null
                ? builder.conjunction()
                : builder.equal(root.get("orderStatus"), OrderStatus.valueOf(status));
    }
}
