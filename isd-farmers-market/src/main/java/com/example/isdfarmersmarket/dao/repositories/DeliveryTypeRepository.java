package com.example.isdfarmersmarket.dao.repositories;

import com.example.isdfarmersmarket.dao.enums.DeliveryTypes;
import com.example.isdfarmersmarket.dao.models.DeliveryTypeFarmer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryTypeRepository extends JpaRepository<DeliveryTypeFarmer, Integer> {
    Optional<DeliveryTypeFarmer> findByFarmerEmailAndType(String email, DeliveryTypes type);
    Optional<DeliveryTypeFarmer> findByFarmerIdAndType(Long id, DeliveryTypes type);
    Optional<DeliveryTypeFarmer> getDeliveryTypeFarmerById(Long id);
    List<DeliveryTypeFarmer> findByFarmerEmail(String email);
}
