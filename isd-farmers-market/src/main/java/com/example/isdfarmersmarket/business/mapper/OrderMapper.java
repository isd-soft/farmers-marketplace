package com.example.isdfarmersmarket.business.mapper;

import com.example.isdfarmersmarket.dao.models.Order;
import com.example.isdfarmersmarket.web.dto.OrderDTO;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDTO map(Order order);
    List<OrderDTO> mapOrders(List<Order> orders);
}
