package com.example.isdfarmersmarket.web.dto;
import com.example.isdfarmersmarket.dao.models.DeliveryTypeFarmer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
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
    private DeliveryTypeDTO deliveryTypeFarmer;
    private BigDecimal deliveryPrice;
}
