package com.example.isdfarmersmarket.web.controllers;

import com.example.isdfarmersmarket.business.services.interfaces.PlannedOrderService;
import com.example.isdfarmersmarket.web.commands.PlannedOrderActivityCommand;
import com.example.isdfarmersmarket.web.commands.ScheduleOrderCommand;
import com.example.isdfarmersmarket.web.commands.UpdateScheduledOrderCommand;
import com.example.isdfarmersmarket.web.dto.PlannedOrderDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/planned-order")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlannedOrderController {
    PlannedOrderService plannedOrderService;

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    public ResponseEntity<PlannedOrderDTO> scheduleOrder(@RequestBody ScheduleOrderCommand scheduleOrderCommand) {
        return ResponseEntity.status(CREATED).body(plannedOrderService.scheduleOrder(scheduleOrderCommand));
    }
    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<PlannedOrderDTO> updatePlannedOrder(@PathVariable Long id, @RequestBody UpdateScheduledOrderCommand updateScheduledOrderCommand) {
        return ResponseEntity.status(OK).body(plannedOrderService.updateScheduledOrder(id, updateScheduledOrderCommand));
    }
    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping(value = "/active/{id}")
    public ResponseEntity<PlannedOrderDTO> setActivityState(@PathVariable Long id, @RequestBody PlannedOrderActivityCommand plannedOrderActivityCommand) {
        return ResponseEntity.status(OK).body(plannedOrderService.changeActivityState(id, plannedOrderActivityCommand.isActive()));
    }
    @GetMapping("/management")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<PlannedOrderDTO>> getCurrentUserPlannedOrders() {
        return ResponseEntity.status(OK).body(plannedOrderService.getCurrentUserPlannedOrders());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<PlannedOrderDTO> deletePlannedOrder(@PathVariable Long id) {
        return ResponseEntity.status(OK).body(plannedOrderService.deletePlannedOrder(id));
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<PlannedOrderDTO> getPlannedOrderById(@PathVariable Long id) {
        return ResponseEntity.status(OK).body(plannedOrderService.getPlannedOrderById(id));
    }
}
