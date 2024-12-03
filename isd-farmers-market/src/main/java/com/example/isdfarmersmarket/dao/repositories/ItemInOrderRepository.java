package com.example.isdfarmersmarket.dao.repositories;

import com.example.isdfarmersmarket.dao.models.Image;
import com.example.isdfarmersmarket.dao.models.ItemInOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemInOrderRepository extends JpaRepository<ItemInOrder, Long> {
}
