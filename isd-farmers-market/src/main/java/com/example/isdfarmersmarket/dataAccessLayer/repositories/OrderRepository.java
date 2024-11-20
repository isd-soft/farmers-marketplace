package com.example.isdfarmersmarket.repositories;

import com.example.isdfarmersmarket.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}

