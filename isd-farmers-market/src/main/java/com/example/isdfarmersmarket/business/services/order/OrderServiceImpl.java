package com.example.isdfarmersmarket.business.services.order;

import com.example.isdfarmersmarket.business.events.OrderConfirmedEvent;
import com.example.isdfarmersmarket.business.events.OrderPlacedEvent;
import com.example.isdfarmersmarket.business.exception.custom_exceptions.EntityNotFoundException;
import com.example.isdfarmersmarket.business.mapper.ItemInCartMapper;
import com.example.isdfarmersmarket.business.mapper.ItemInOrderMapper;
import com.example.isdfarmersmarket.business.mapper.OrderMapper;
import com.example.isdfarmersmarket.business.security.JwtPrincipal;
import com.example.isdfarmersmarket.business.services.EventPublisher;
import com.example.isdfarmersmarket.business.utils.SecurityUtils;
import com.example.isdfarmersmarket.dao.enums.OrderStatus;
import com.example.isdfarmersmarket.dao.models.*;
import com.example.isdfarmersmarket.dao.repositories.*;
import com.example.isdfarmersmarket.dao.specifications.OrderSpecification;
import com.example.isdfarmersmarket.dao.specifications.ProductSpecification;
import com.example.isdfarmersmarket.web.commands.order.UpdateOrderCommand;
import com.example.isdfarmersmarket.web.dto.OrderDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    ItemInCartRepository itemInCartRepository;
    OrderMapper orderMapper;
    ItemInCartMapper itemInCartMapper;
    ItemInOrderMapper itemInOrderMapper;
    EventPublisher eventPublisher;

    @Override
    @Transactional
    public List<OrderDTO> createOrders() {
        JwtPrincipal principal = SecurityUtils.getPrincipal();

        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new EntityNotFoundException(principal.getId(), User.class));

        List<ItemInCart> itemsInCart = itemInCartRepository.getAllByUser(user);
        List<ItemInOrder> itemsInOrder = itemsInCart.stream()
                .map(item -> new ItemInOrder(item.getProduct(), item.getQuantity(), item.getProduct().getPricePerUnit()))
                .toList();

        Map<Long, List<ItemInOrder>> groupedByFarmer = itemsInOrder.stream()
                .collect(Collectors.groupingBy(item -> item.getProduct().getFarmer().getId()));

//        groupedByFarmer.forEach((farmerId, items) -> {
//            BigDecimal totalPrice = new BigDecimal(0);
//            items.forEach(item -> {
//                item.setPricePerUnit(item.getProduct().getPricePerUnit().subtract(item.getProduct()
//                        .getPricePerUnit().multiply(BigDecimal.valueOf(item.getProduct().getDiscountPercents() / 100))));
//                item.getProduct().setQuantity(item.getProduct().getQuantity() - item.getQuantity());
//            });
//
//            for (var item : items) {
//                totalPrice = totalPrice.add(item.getPricePerUnit().multiply(BigDecimal.valueOf(item.getQuantity())));
//            }


            groupedByFarmer.forEach((farmerId, items) -> {
                BigDecimal totalPrice = items.stream()
                        .peek(item -> {
                            BigDecimal discountMultiplier = BigDecimal.valueOf(1 - item.getProduct().getDiscountPercents() / 100.0);
                            item.setPricePerUnit(item.getProduct().getPricePerUnit().multiply(discountMultiplier));

                            item.getProduct().setQuantity(item.getProduct().getQuantity() - item.getQuantity());
                        })
                        .map(item -> item.getPricePerUnit().multiply(BigDecimal.valueOf(item.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);


            Order order = new Order(user, userRepository.findById(farmerId)
                    .orElseThrow(() -> new EntityNotFoundException(farmerId, Order.class)),
                    OrderStatus.PENDING,
                    totalPrice);

            orderRepository.save(order);

            items.forEach(item -> item.setOrder(order));
            itemInOrderRepository.saveAll(items);

            OrderPlacedEvent event = new OrderPlacedEvent(this, order, items);
            eventPublisher.publishEvent(event);
        });

        itemInCartRepository.deleteAllByUser(user);
        return Collections.emptyList();
    }


    @Override
    @Transactional
    public OrderDTO updateOrder(Long id, UpdateOrderCommand updateOrderCommand) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new com.example.isdfarmersmarket.business.exception.custom_exceptions.EntityNotFoundException(id, Order.class));

        order.setOrderStatus(OrderStatus.valueOf(updateOrderCommand.getOrderStatus().toUpperCase()));

        if (OrderStatus.CONFIRMED.equals(order.getOrderStatus())) {
            List<ItemInOrder> items = new ArrayList<>(order.getItemsInOrder());
            OrderConfirmedEvent event = new OrderConfirmedEvent(this, order, items);
            eventPublisher.publishEvent(event);
        }

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
        List<OrderDTO> orderDTOList = orderMapper.mapOrders(orderRepository.findAll());
        orderDTOList.forEach(orderDTO -> {
            Order order = orderRepository.findById(orderDTO.getId()).orElseThrow();
            Set<ItemInOrder> itemInOrders = order.getItemsInOrder();
            orderDTO.setItemsInOrder(itemInOrderMapper.mapOrders(itemInOrders));
        });
        return orderDTOList;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<OrderDTO> getCurrentUserOrders(String orderStatus, Pageable pageable) {
        JwtPrincipal principal = SecurityUtils.getPrincipal();
        Long customerId = principal.getId();
        Specification<Order> filters = Specification
                .where(orderStatus == null ? null : OrderSpecification.statusIs(orderStatus))
                .and((customerId == null || customerId == 0L) ? null : OrderSpecification.customerIs(customerId));
        Page<OrderDTO> orderDTOPage = orderRepository.findAll(filters, pageable)
                .map(orderMapper::map);
        orderDTOPage.forEach(orderDTO -> {
            Order order = orderRepository.findById(orderDTO.getId()).orElseThrow();
            Set<ItemInOrder> itemInOrders = order.getItemsInOrder();
            orderDTO.setItemsInOrder(itemInOrderMapper.mapOrders(itemInOrders));
        });
        return orderDTOPage;
    }
}
