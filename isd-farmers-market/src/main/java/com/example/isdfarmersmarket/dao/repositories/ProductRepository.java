package com.example.isdfarmersmarket.dao.repositories;

import com.example.isdfarmersmarket.dao.models.ItemInOrder;
import com.example.isdfarmersmarket.dao.models.Product;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
}
