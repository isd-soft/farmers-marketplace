package com.example.isdfarmersmarket.business.services;

import com.example.isdfarmersmarket.dao.enums.DeliveryTypes;
import com.example.isdfarmersmarket.web.commands.CreateDeliveryTypeCommand;
import com.example.isdfarmersmarket.web.commands.UpdateDeliveryTypeCommand;
import com.example.isdfarmersmarket.web.dto.DeliveryTypeDTO;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

public interface DeliveryTypeService {
    DeliveryTypeDTO createDeliveryType(CreateDeliveryTypeCommand createDeliveryTypeCommand);

    DeliveryTypeDTO updateDeliveryType(UpdateDeliveryTypeCommand updateDeliveryTypeCommand);

    DeliveryTypeDTO deleteDeliveryType(Long id);

    List<DeliveryTypeDTO> getAllDeliveryType();
    List<String> getAllCurrentUserDeliveryType();

    @Transactional(readOnly = true)
    BigDecimal getDeliveryTypePrice(String email, DeliveryTypes deliveryType);
}
