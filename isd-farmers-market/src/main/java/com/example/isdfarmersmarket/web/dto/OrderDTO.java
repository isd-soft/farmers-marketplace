package com.example.isdfarmersmarket.web.dto;


import com.example.isdfarmersmarket.dao.enums.OrderStatus;
import com.example.isdfarmersmarket.dao.models.ItemInOrder;
import com.example.isdfarmersmarket.dao.models.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class OrderDTO {
    private Long id;
    private OrderStatus orderStatus;
//    private UserDTO user;
    private BigDecimal totalPrice;
//    private Set<productsDTO> products = new HashSet<>();
    private LocalDateTime createdDate = LocalDateTime.now();
}
