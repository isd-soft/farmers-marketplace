package com.example.isdfarmersmarket.dao.repositories;

import com.example.isdfarmersmarket.dao.models.ItemInCart;
import com.example.isdfarmersmarket.dao.models.Order;
import com.example.isdfarmersmarket.dao.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
//    List<Order> getAllByUser(User customer);
}