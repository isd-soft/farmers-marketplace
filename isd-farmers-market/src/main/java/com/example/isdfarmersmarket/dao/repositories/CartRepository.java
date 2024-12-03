package com.example.isdfarmersmarket.dao.repositories;

import com.example.isdfarmersmarket.dao.models.ItemInCart;
import com.example.isdfarmersmarket.dao.models.Product;
import com.example.isdfarmersmarket.dao.models.User;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<ItemInCart, Long> {
    boolean existsByUserAndProduct(User user, Product product);
    List<ItemInCart> getAllByUser(User user);
    void deleteAllByUser(User user);
}
