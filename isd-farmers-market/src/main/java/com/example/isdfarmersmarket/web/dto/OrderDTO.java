package com.example.isdfarmersmarket.web.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Builder
@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDTO {
    Long id;
    String orderStatus;
    Long userId;
    BigDecimal totalPrice;
    Set<ItemInOrderDTO> products = new HashSet<>();
    LocalDateTime createdDate = LocalDateTime.now();
}
