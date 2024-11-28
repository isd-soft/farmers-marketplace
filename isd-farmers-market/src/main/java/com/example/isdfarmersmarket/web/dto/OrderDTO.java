package com.example.isdfarmersmarket.web.dto;

import lombok.*;
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
public class OrderDTO {
    private Long id;
    private String orderStatus;
    private Long userId;
    private BigDecimal totalPrice;
    private Set<ItemInOrderDTO> products = new HashSet<>();
    private LocalDateTime createdDate = LocalDateTime.now();
}
