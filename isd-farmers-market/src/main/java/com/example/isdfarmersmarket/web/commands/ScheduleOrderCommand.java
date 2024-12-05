package com.example.isdfarmersmarket.web.commands;

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
}
