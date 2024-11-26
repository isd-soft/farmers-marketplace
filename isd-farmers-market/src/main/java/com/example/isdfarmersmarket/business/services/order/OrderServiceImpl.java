package com.example.isdfarmersmarket.business.services.order;

import com.example.isdfarmersmarket.business.mapper.OrderMapper;
import com.example.isdfarmersmarket.dao.enums.OrderStatus;
import com.example.isdfarmersmarket.dao.models.*;
import com.example.isdfarmersmarket.dao.repositories.OrderRepository;
import com.example.isdfarmersmarket.dao.repositories.ProductRepository;
import com.example.isdfarmersmarket.dao.repositories.UserRepository;
import com.example.isdfarmersmarket.web.commands.order.CreateOrderCommand;
import com.example.isdfarmersmarket.web.commands.order.UpdateOrderCommand;
import com.example.isdfarmersmarket.web.dto.OrderDTO;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private static final String ORDER_FIND_FAILED_BY_ID = "Order with the specified user id not found";

    @Override
    @Transactional
    public OrderDTO createOrder(CreateOrderCommand createOrderCommand, String authenticatedUserEmail) {
        User user = userRepository.findByEmail(authenticatedUserEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + authenticatedUserEmail));

        Order order = Order.builder()
                .user(user)
                .orderStatus(createOrderCommand.getOrderStatus())
                .totalPrice(BigDecimal.ZERO)
                .createdDate(LocalDateTime.now())
                .build();

        Set<ItemInOrder> items = createOrderCommand.getProductsId().stream()
                .map(productItem -> {
                    Product product = productRepository.findById(productItem.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productItem.getId()));

                    ItemInOrder item = new ItemInOrder();
                    item.setProduct(product);
                    item.setPricePerUnit(product.getPricePerUnit());
                    item.setQuantity(productItem.getQuantity());
                    item.setOrder(order);

                    return item;
                })
                .collect(Collectors.toSet());

        order.setProducts(items);

        BigDecimal totalPrice = items.stream()
                .map(item -> item.getPricePerUnit().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalPrice(totalPrice);

        return orderMapper.map(orderRepository.save(order));
    }

    @Override
    @Transactional
    public OrderDTO updateOrder(Long id, UpdateOrderCommand updateOrderCommand) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ORDER_FIND_FAILED_BY_ID));

        order.setOrderStatus(OrderStatus.valueOf(updateOrderCommand.getOrderStatus().toUpperCase()));
        return orderMapper.map(orderRepository.save(order));
    }


    @Override
    public OrderDTO deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ORDER_FIND_FAILED_BY_ID));
        orderRepository.delete(order);
        return orderMapper.map(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getAllOrders() {
        List<Order> order = orderRepository.findAll();
        return orderMapper.mapOrders(order);
    }

    @Override
    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ORDER_FIND_FAILED_BY_ID));
        return orderMapper.map(order);
    }
}
