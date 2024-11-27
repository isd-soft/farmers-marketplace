package com.example.isdfarmersmarket.dao.repositories;

import com.example.isdfarmersmarket.dao.models.DeliveryTypeFarmer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryTypeRepository extends JpaRepository<DeliveryTypeFarmer, Integer> {
    Optional<DeliveryTypeFarmer> getDeliveryTypeFarmerById(Long id);
}
