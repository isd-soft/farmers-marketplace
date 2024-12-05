package com.example.isdfarmersmarket.web.dto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class PlannedOrderDTO {
    private Long id;
    private UserProfileDTO customer;
    private ProductDTO product;
    private int quantity;
    private DayOfWeek dayOfWeek;
    private LocalTime time;
    private boolean active;
    private LocalDateTime createdDate;
}
