package com.example.isdfarmersmarket.business.services.interfaces;

import com.example.isdfarmersmarket.web.commands.ScheduleOrderCommand;
import com.example.isdfarmersmarket.web.commands.UpdateScheduledOrderCommand;
import com.example.isdfarmersmarket.web.dto.PlannedOrderDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PlannedOrderService {
    PlannedOrderDTO scheduleOrder(ScheduleOrderCommand scheduleOrderCommand);

    @Transactional
    PlannedOrderDTO updateScheduledOrder(Long id, UpdateScheduledOrderCommand updateScheduledOrderCommand);

    @Transactional
    PlannedOrderDTO changeActivityState(Long id, boolean state);

    @Transactional
    PlannedOrderDTO deletePlannedOrder(Long id);

    @Transactional
    List<PlannedOrderDTO> getCurrentUserPlannedOrders();

    @Transactional
    PlannedOrderDTO getPlannedOrderById(Long id);

    void processPlannedOrders();
}
