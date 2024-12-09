package com.example.isdfarmersmarket.dao.repositories;

import com.example.isdfarmersmarket.dao.enums.OrderStatus;
import com.example.isdfarmersmarket.dao.models.Order;
import com.example.isdfarmersmarket.dao.models.Product;
import com.example.isdfarmersmarket.dao.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
        @Query("SELECT COUNT(i) FROM ItemInOrder i WHERE i.product.id = :productId")
        long countOrdersByProductId(@Param("productId") Long productId);

        Page<Order> findAllByCustomerId(Long customerId, Specification<Order> filters, Pageable pageable);

        @Query("""
            SELECT COUNT(o) > 0
            FROM Order o
            WHERE o.customer = :customer
              AND o.farmer = :farmer
              AND o.orderStatus = 'COMPLETED'
            """)
        boolean hasCustomerCompletedOrderWithFarmer(User customer, User farmer);

        @Query("""
            SELECT COUNT(1) > 0
            FROM Order o
            JOIN o.itemsInOrder i
            WHERE o.customer = :customer
              AND i.product = :product
              AND o.orderStatus = 'COMPLETED'
            """)
        boolean hasCustomerCompletedOrderWithProduct(User customer, Product product);
}