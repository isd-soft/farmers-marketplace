package com.example.isdfarmersmarket.web.commands;

import com.example.isdfarmersmarket.dao.enums.DeliveryTypes;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
public class ScheduleOrderCommand {
    @NotNull
    private Long productId;
    @NotNull
    private int quantity;
    @NotNull
    private DayOfWeek dayOfWeek;
    @NotNull
    private LocalTime time;
    @NotBlank
    DeliveryTypes deliveryType;
}
