package com.example.isdfarmersmarket.business.services.order;

import com.example.isdfarmersmarket.business.security.JwtPrincipal;
import com.example.isdfarmersmarket.web.commands.order.CreateOrderCommand;
import com.example.isdfarmersmarket.web.commands.order.UpdateOrderCommand;
import com.example.isdfarmersmarket.web.dto.OrderDTO;
import com.example.isdfarmersmarket.web.dto.ProductDTO;

import java.util.List;

public interface OrderService {
    OrderDTO createOrder(CreateOrderCommand createOrderCommand);
    OrderDTO updateOrder(Long id, UpdateOrderCommand updateOrderCommand);
    OrderDTO deleteOrder(Long id);
    OrderDTO getOrderById( Long id);
    List<OrderDTO> getAllOrders();
}
