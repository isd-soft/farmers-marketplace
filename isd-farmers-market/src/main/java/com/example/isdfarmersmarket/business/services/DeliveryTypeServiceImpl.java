package com.example.isdfarmersmarket.business.services;


import com.example.isdfarmersmarket.business.mapper.DaoMapper;
import com.example.isdfarmersmarket.business.security.JwtPrincipal;
import com.example.isdfarmersmarket.dao.enums.DeliveryTypes;
import com.example.isdfarmersmarket.dao.models.DeliveryTypeFarmer;
import com.example.isdfarmersmarket.dao.models.User;
import com.example.isdfarmersmarket.dao.repositories.DeliveryTypeRepository;
import com.example.isdfarmersmarket.dao.repositories.UserRepository;
import com.example.isdfarmersmarket.web.commands.CreateDeliveryTypeCommand;
import com.example.isdfarmersmarket.web.commands.UpdateDeliveryTypeCommand;
import com.example.isdfarmersmarket.web.dto.DeliveryTypeDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryTypeServiceImpl implements DeliveryTypeService {
    private final DeliveryTypeRepository deliveryTypeRepository;
    private final UserRepository userRepository;
    private final DaoMapper daoMapper;

    @Override
    @Transactional
    public DeliveryTypeDTO createDeliveryType(JwtPrincipal jwtPrincipal, CreateDeliveryTypeCommand createDeliveryTypeCommand) {
        BigDecimal price = createDeliveryTypeCommand.getPrice();
        DeliveryTypes deliveryTypes = createDeliveryTypeCommand.getTypes();
        User farmer = userRepository.findByEmail(jwtPrincipal.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Farmer with this email doesn't exist"));

        DeliveryTypeFarmer deliveryTypeFarmer = DeliveryTypeFarmer.builder()
                .price(price).type(deliveryTypes).farmer(farmer)
                .build();
        deliveryTypeRepository.save(deliveryTypeFarmer);
        return daoMapper.map(deliveryTypeFarmer);
    }

    @Override
    @Transactional
    public DeliveryTypeDTO updateDeliveryType(JwtPrincipal jwtPrincipal, Long id, UpdateDeliveryTypeCommand updateDeliveryTypeCommand) {
        DeliveryTypeFarmer existingDeliveryType = deliveryTypeRepository.getDeliveryTypeFarmerById(id)
                .orElseThrow(() -> new EntityNotFoundException("Delivery type with the specified id not found")
                );
        BigDecimal price = updateDeliveryTypeCommand.getPrice();
        DeliveryTypes deliveryTypes = updateDeliveryTypeCommand.getTypes();
        DeliveryTypeFarmer deliveryTypeFarmer = DeliveryTypeFarmer.builder()
                .price(price).type(deliveryTypes).farmer(existingDeliveryType.getFarmer())
                .build();
        if (!existingDeliveryType.getFarmer().getEmail().equals(jwtPrincipal.getEmail())) {
            throw new IllegalArgumentException("You can't update other people's delivery types");
        }
        deliveryTypeRepository.save(deliveryTypeFarmer);
        return daoMapper.map(deliveryTypeFarmer);
    }

    @Override
    @Transactional
    public DeliveryTypeDTO deleteDeliveryType(JwtPrincipal jwtPrincipal, Long id) {
        DeliveryTypeFarmer deliveryTypeFarmer = deliveryTypeRepository.getDeliveryTypeFarmerById(id)
                .orElseThrow(() -> new EntityNotFoundException("Delivery type with the specified id not found")
                );
        if (!deliveryTypeFarmer.getFarmer().getEmail().equals(jwtPrincipal.getEmail())) {
            throw new IllegalArgumentException("You can't delete other people's delivery types");
        }
        deliveryTypeRepository.delete(deliveryTypeFarmer);
        return daoMapper.map(deliveryTypeFarmer);
    }

    @Override
    @Transactional
    public List<DeliveryTypeDTO> getAllDeliveryType() {
        List<DeliveryTypeFarmer> deliveryTypeFarmerList = deliveryTypeRepository.findAll();
        return daoMapper.mapDeliveryTypes(deliveryTypeFarmerList);
    }
}
