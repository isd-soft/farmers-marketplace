package com.example.isdfarmersmarket.business.mapper;

import com.example.isdfarmersmarket.dao.models.PlannedOrder;
import com.example.isdfarmersmarket.web.dto.PlannedOrderDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PlannedOrderMapper {
    PlannedOrderDTO map(PlannedOrder plannedOrder);
    List<PlannedOrderDTO> mapToPlannedOrders(List<PlannedOrder> plannedOrders);
}
