package com.example.isdfarmersmarket.dao.repositories;

import com.example.isdfarmersmarket.dao.models.Order;
import com.example.isdfarmersmarket.dao.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
//    List<Order> getAllByUser(User user);
        @Query("SELECT COUNT(i) FROM ItemInOrder i WHERE i.product.id = :productId")
        long countOrdersByProductId(@Param("productId") Long productId);

        Page<Order> findAllByCustomerId(Long customerId, Pageable pageable);
        List<Order> findAllByCustomerId(Long customerId);
}