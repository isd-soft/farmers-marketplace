package com.example.isdfarmersmarket.business.services;

import com.example.isdfarmersmarket.web.commands.CreateDeliveryTypeCommand;
import com.example.isdfarmersmarket.web.commands.UpdateDeliveryTypeCommand;
import com.example.isdfarmersmarket.web.dto.DeliveryTypeDTO;

import java.util.List;

public interface DeliveryTypeService {
    DeliveryTypeDTO createDeliveryType(CreateDeliveryTypeCommand createDeliveryTypeCommand);

    DeliveryTypeDTO updateDeliveryType(UpdateDeliveryTypeCommand updateDeliveryTypeCommand);

    DeliveryTypeDTO deleteDeliveryType(Long id);

    List<DeliveryTypeDTO> getAllDeliveryType();
}
