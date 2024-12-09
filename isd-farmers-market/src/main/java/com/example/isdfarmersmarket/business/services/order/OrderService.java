package com.example.isdfarmersmarket.business.services.order;

import com.example.isdfarmersmarket.web.commands.order.CreateOrderCommand;
import com.example.isdfarmersmarket.web.commands.order.UpdateOrderCommand;
import com.example.isdfarmersmarket.web.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderService {
    List<OrderDTO> createOrders(CreateOrderCommand createOrderCommand);
    OrderDTO farmerStatusChangeOrder(Long id, UpdateOrderCommand updateOrderCommand);

    @Transactional
    OrderDTO customerReceivedOrder(Long id);

    OrderDTO deleteOrder(Long id);
    OrderDTO getOrderById( Long id);
    List<OrderDTO> getAllOrders();

    @Transactional(readOnly = true)
    Page<OrderDTO> getCurrentUserOrders(String status, Pageable pageable);

    @Transactional(readOnly = true)
    Page<OrderDTO> getCurrentFarmerOrders(String orderStatus, Pageable pageable);
}
