package com.example.isdfarmersmarket.business.services;


import com.example.isdfarmersmarket.business.mapper.DaoMapper;
import com.example.isdfarmersmarket.business.security.JwtPrincipal;
import com.example.isdfarmersmarket.business.utils.SecurityUtils;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DeliveryTypeServiceImpl implements DeliveryTypeService {
    private final DeliveryTypeRepository deliveryTypeRepository;
    private final UserRepository userRepository;
    private final DaoMapper daoMapper;

    @Override
    @Transactional
    public DeliveryTypeDTO createDeliveryType(CreateDeliveryTypeCommand createDeliveryTypeCommand) {
        BigDecimal price = createDeliveryTypeCommand.getPrice();
        DeliveryTypes deliveryTypes = createDeliveryTypeCommand.getType();
        JwtPrincipal principal = (JwtPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User farmer = userRepository.findByEmail(principal.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Farmer with this email doesn't exist"));

        DeliveryTypeFarmer deliveryTypeFarmer = DeliveryTypeFarmer.builder()
                .price(price).type(deliveryTypes).farmer(farmer)
                .build();
        deliveryTypeRepository.save(deliveryTypeFarmer);
        return daoMapper.map(deliveryTypeFarmer);
    }

    @Override
    @Transactional
    public DeliveryTypeDTO updateDeliveryType(UpdateDeliveryTypeCommand updateDeliveryTypeCommand) {
        JwtPrincipal principal = (JwtPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        System.out.println(principal.getEmail());
        System.out.println(updateDeliveryTypeCommand.getType());

        DeliveryTypeFarmer existingDeliveryType =
                deliveryTypeRepository.findByFarmerEmailAndType(principal.getEmail(), updateDeliveryTypeCommand.getType())
                .orElseThrow();

        existingDeliveryType.setPrice(updateDeliveryTypeCommand.getPrice());
        deliveryTypeRepository.save(existingDeliveryType);
        return daoMapper.map(existingDeliveryType);
    }

    @Override
    @Transactional
    public DeliveryTypeDTO deleteDeliveryType(Long id) {
        DeliveryTypeFarmer deliveryTypeFarmer = deliveryTypeRepository.getDeliveryTypeFarmerById(id)
                .orElseThrow(() -> new EntityNotFoundException("Delivery type with the specified id not found")
                );
        JwtPrincipal principal = (JwtPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!deliveryTypeFarmer.getFarmer().getEmail().equals(principal.getEmail())) {
            throw new IllegalArgumentException("You can't delete other people's delivery types");
        }
        deliveryTypeRepository.delete(deliveryTypeFarmer);
        return daoMapper.map(deliveryTypeFarmer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryTypeDTO> getAllDeliveryType() {
        JwtPrincipal principal = (JwtPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User farmer = userRepository.findByEmail(principal.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Farmer with this email doesn't exist"));

        List<DeliveryTypes> allDeliveryTypes = List.of(DeliveryTypes.values());
        List<DeliveryTypeFarmer> deliveryTypesForUser = deliveryTypeRepository.findByFarmerEmail(farmer.getEmail());
        List<DeliveryTypeDTO> result = new ArrayList<>(deliveryTypesForUser.stream()
                .map(deliveryTypeFarmer -> {
                    DeliveryTypeDTO dto = daoMapper.map(deliveryTypeFarmer);
                    dto.setExistsForUser(true);
                    return dto;
                })
                .toList());

        for (DeliveryTypes types: allDeliveryTypes) {
            boolean exists = deliveryTypesForUser.stream()
                    .anyMatch(deliveryTypeFarmer -> deliveryTypeFarmer.getType() == types);

            if (!exists) {
                DeliveryTypeDTO newDeliveryTypeDTO = new DeliveryTypeDTO();
                newDeliveryTypeDTO.setType(types);
                newDeliveryTypeDTO.setPrice(BigDecimal.ZERO);
                newDeliveryTypeDTO.setExistsForUser(false);
                result.add(newDeliveryTypeDTO);
            }
        }
        return result;
    }
    @Override
    @Transactional(readOnly = true)
    public List<String> getAllCurrentUserDeliveryType() {
        JwtPrincipal principal = SecurityUtils.getPrincipal();
        return deliveryTypeRepository.findByFarmerEmail(principal.getEmail())
                .stream().map(deliveryTypeFarmer -> {
                    return deliveryTypeFarmer.getType().name();
                })
                .toList();
    }
    @Transactional(readOnly = true)
    @Override
    public BigDecimal getDeliveryTypePrice(String email, DeliveryTypes deliveryType) {
        Optional<DeliveryTypeFarmer> deliveryTypeOptional = deliveryTypeRepository.findByFarmerEmailAndType(email, deliveryType);
        return deliveryTypeOptional.map(DeliveryTypeFarmer::getPrice).orElseThrow(() -> new EntityNotFoundException("Delivery type not found."));
    }
}
