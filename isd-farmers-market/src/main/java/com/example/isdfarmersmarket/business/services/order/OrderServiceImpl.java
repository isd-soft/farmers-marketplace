package com.example.isdfarmersmarket.business.services.order;

//import com.example.isdfarmersmarket.business.listeners.OrderConfirmedListener;

import com.example.isdfarmersmarket.business.mapper.ItemInCartMapper;
import com.example.isdfarmersmarket.business.mapper.ItemInOrderMapper;
import com.example.isdfarmersmarket.business.mapper.OrderMapper;
import com.example.isdfarmersmarket.business.security.JwtPrincipal;
import com.example.isdfarmersmarket.business.utils.SecurityUtils;
import com.example.isdfarmersmarket.dao.enums.OrderStatus;
import com.example.isdfarmersmarket.dao.models.*;
import com.example.isdfarmersmarket.dao.repositories.*;
import com.example.isdfarmersmarket.web.commands.order.UpdateOrderCommand;
import com.example.isdfarmersmarket.web.dto.ItemInOrderDTO;
import com.example.isdfarmersmarket.web.dto.OrderDTO;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {
    OrderRepository orderRepository;
    ProductRepository productRepository;
    UserRepository userRepository;
    ItemInOrderRepository itemInOrderRepository;
    CartRepository cartRepository;
    OrderMapper orderMapper;
    ItemInCartMapper itemInCartMapper;
    ItemInOrderMapper itemInOrderMapper;
//    private final OrderConfirmedListener orderConfirmedListener;

    @Override
    @Transactional
    public List<OrderDTO> createOrders() {
        JwtPrincipal principal = SecurityUtils.getPrincipal();

        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new com.example.isdfarmersmarket.business.exception.custom_exceptions.EntityNotFoundException(principal.getId(), User.class));

        List<ItemInCart> itemsInCart = cartRepository.getAllByUser(user);
        List<ItemInOrder> itemsInOrder = itemInCartMapper.mapToItemInOrder(itemsInCart);

        Map<Long, List<ItemInOrder>> groupedByFarmer = itemsInOrder.stream()
                .collect(Collectors.groupingBy(item -> item.getProduct().getFarmer().getId()));

        List<Order> orders = groupedByFarmer.entrySet().stream()
                .map(entry -> {
                    Long farmerId = entry.getKey();
                    List<ItemInOrder> items = entry.getValue();

                    BigDecimal totalPrice = items.stream()
                            .map(item -> item.getPricePerUnit().multiply(BigDecimal.valueOf(item.getQuantity())))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    Order order = Order.builder()
                            .customer(user)
                            .farmer(userRepository.findById(farmerId)
                                    .orElseThrow(() -> new com.example.isdfarmersmarket.business.exception.custom_exceptions.EntityNotFoundException(farmerId, Order.class)))
                            .orderStatus(OrderStatus.PENDING)
                            .totalPrice(totalPrice)
                            .createdDate(LocalDateTime.now())
                            .build();

                    orderRepository.save(order);

                    items.forEach(item -> item.setOrder(order));
                    itemInOrderRepository.saveAll(items);

                    return order;
                })
                .toList();

        cartRepository.deleteAllByUser(user);
        return orderMapper.mapOrders(orders);
    }


    @Override
    @Transactional
    public OrderDTO updateOrder(Long id, UpdateOrderCommand updateOrderCommand) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new com.example.isdfarmersmarket.business.exception.custom_exceptions.EntityNotFoundException(id, Order.class));

        order.setOrderStatus(OrderStatus.valueOf(updateOrderCommand.getOrderStatus().toUpperCase()));
//        orderConfirmedListener.handleOnConfirmedOrder(order);
        return orderMapper.map(orderRepository.save(order));
    }


    @Override
    @Transactional
    public OrderDTO deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new com.example.isdfarmersmarket.business.exception.custom_exceptions.EntityNotFoundException(id, Order.class));
        orderRepository.delete(order);
        return orderMapper.map(order);
    }

    @Override
    @Transactional
    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new com.example.isdfarmersmarket.business.exception.custom_exceptions.EntityNotFoundException(id, Order.class));

        return orderMapper.map(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getAllOrders() {
        JwtPrincipal principal = SecurityUtils.getPrincipal();
        User authenticatedUser = userRepository.findById(principal.getId())
                .orElseThrow(() -> new com.example.isdfarmersmarket.business.exception.custom_exceptions.EntityNotFoundException(principal.getId(), User.class));
//           List<Order> orders = orderRepository.getAllByUser(authenticatedUser);
        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(order -> {
                    List<ItemInOrderDTO> itemInOrderDTOs = itemInOrderMapper.mapOrders(order.getItemsInOrder().stream().toList());

                    return OrderDTO.builder()
                            .id(order.getId())
                            .orderStatus(order.getOrderStatus().name())
                            .userId(authenticatedUser.getId())
                            .totalPrice(order.getTotalPrice())
                            .products(itemInOrderDTOs.stream().collect(Collectors.toSet()))
                            .createdDate(order.getCreatedDate())
                            .build();
                }).toList();
    }
}
