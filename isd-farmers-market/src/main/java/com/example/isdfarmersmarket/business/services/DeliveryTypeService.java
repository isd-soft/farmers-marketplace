package com.example.isdfarmersmarket.business.services;

import com.example.isdfarmersmarket.business.security.JwtPrincipal;
import com.example.isdfarmersmarket.web.commands.CreateDeliveryTypeCommand;
import com.example.isdfarmersmarket.web.commands.UpdateDeliveryTypeCommand;
import com.example.isdfarmersmarket.web.dto.DeliveryTypeDTO;

import java.util.List;

public interface DeliveryTypeService {
    DeliveryTypeDTO createDeliveryType(JwtPrincipal jwtPrincipal, CreateDeliveryTypeCommand createDeliveryTypeCommand);

    DeliveryTypeDTO updateDeliveryType(JwtPrincipal jwtPrincipal, Long id, UpdateDeliveryTypeCommand updateDeliveryTypeCommand);

    DeliveryTypeDTO deleteDeliveryType(JwtPrincipal jwtPrincipal, Long id);

    List<DeliveryTypeDTO> getAllDeliveryType();
}
