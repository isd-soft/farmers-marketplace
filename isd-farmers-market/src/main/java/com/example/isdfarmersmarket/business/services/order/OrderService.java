package com.example.isdfarmersmarket.business.services.order;

import com.example.isdfarmersmarket.web.commands.order.CreateOrderCommand;
import com.example.isdfarmersmarket.web.commands.order.UpdateOrderCommand;
import com.example.isdfarmersmarket.web.dto.OrderDTO;

import java.util.List;

public interface OrderService {
    OrderDTO createOrder(CreateOrderCommand createOrderCommand, String authenticatedUsername);
    OrderDTO updateOrder(Long id, UpdateOrderCommand updateOrderCommand);
    OrderDTO deleteOrder(Long id);
    List<OrderDTO> getAllOrders();
}
