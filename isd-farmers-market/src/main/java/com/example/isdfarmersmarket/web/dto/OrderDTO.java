package com.example.isdfarmersmarket.web.dto;


import com.example.isdfarmersmarket.dao.enums.OrderStatus;
import com.example.isdfarmersmarket.dao.models.ItemInOrder;
import com.example.isdfarmersmarket.dao.models.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jdk.jshell.Snippet;
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
