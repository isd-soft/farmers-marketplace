package com.example.isdfarmersmarket.business.services.order;

import com.example.isdfarmersmarket.business.mapper.OrderMapper;
import com.example.isdfarmersmarket.business.security.JwtPrincipal;
import com.example.isdfarmersmarket.business.utils.SecurityUtils;
import com.example.isdfarmersmarket.dao.enums.OrderStatus;
import com.example.isdfarmersmarket.dao.models.*;
import com.example.isdfarmersmarket.dao.repositories.*;
import com.example.isdfarmersmarket.web.commands.order.CreateOrderCommand;
import com.example.isdfarmersmarket.web.commands.order.UpdateOrderCommand;
import com.example.isdfarmersmarket.web.dto.ItemInOrderDTO;
import com.example.isdfarmersmarket.web.dto.OrderDTO;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ItemInOrderRepository itemInOrderRepository;
    private final OrderMapper orderMapper;
    private static final String ORDER_FIND_FAILED_BY_ID = "Order with the specified user id not found";
    private final OrderConfirmedListener orderConfirmedListener;

    @Override
    @Transactional
    public OrderDTO createOrder(CreateOrderCommand createOrderCommand) {
//        JwtPrincipal principal = (JwtPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        JwtPrincipal principal = SecurityUtils.getPrincipal();

        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + principal.getId()));

        List<ItemInOrder> items = createOrderCommand.getProductsId().stream()
                .map(productItem -> {
                    Product product = productRepository.findById(productItem.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productItem.getId()));

                    ItemInOrder item = new ItemInOrder();
                    item.setProduct(product);
                    item.setPricePerUnit(product.getPricePerUnit());

                    return item;
                })
                .collect(Collectors.toList());


        itemInOrderRepository.saveAll(items);


        Order order = Order.builder()
                .customer()
                .orderStatus(createOrderCommand.getOrderStatus())
                .totalPrice(BigDecimal.ZERO)
                .createdDate(LocalDateTime.now())
                .build();


        order.setProducts(new HashSet<>(items));

        BigDecimal totalPrice = items.stream()
                .map(item -> item.getPricePerUnit().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalPrice(totalPrice);
        orderRepository.save(order);
        items.forEach(itemInOrder -> itemInOrder.setOrder(order));
        return orderMapper.map(order);
    }


    @Override
    @Transactional
    public OrderDTO updateOrder(Long id, UpdateOrderCommand updateOrderCommand) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ORDER_FIND_FAILED_BY_ID));

        order.setOrderStatus(OrderStatus.valueOf(updateOrderCommand.getOrderStatus().toUpperCase()));

        orderConfirmedListener.handleOnConfirmedOrder(order);
        return orderMapper.map(orderRepository.save(order));
    }


    @Override
    @Transactional
    public OrderDTO deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ORDER_FIND_FAILED_BY_ID));
        orderRepository.delete(order);
        return orderMapper.map(order);
    }

    @Override
    @Transactional
    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ORDER_FIND_FAILED_BY_ID));

        return orderMapper.map(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        JwtPrincipal principal = (JwtPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + principal.getId()));


        return orders.stream()
                .map(order -> {
                    Set<ItemInOrderDTO> itemInOrderDTOs = order.getProducts().stream()
                            .map(itemInOrder -> {
                                Product product =itemInOrder.getProduct();

                                Optional<Image> firstImage = product.getImages().stream().findFirst();
                                String imageBase64 = convertImageToBase64(firstImage);

                                return ItemInOrderDTO.builder()
                                        .productId(product.getId())
                                        .productTitle(product.getTitle())
                                        .productDescription(product.getDescription())
                                        .quantity(itemInOrder.getQuantity())
                                        .pricePerUnit(itemInOrder.getPricePerUnit())
                                        .imageBase64(imageBase64)
                                        .reviewCount(product.getReviewCount())
                                        .rating(product.getRating())
                                        .build();
                            }).collect(Collectors.toSet());

                    return OrderDTO.builder()
                            .id(order.getId())
                            .orderStatus(order.getOrderStatus().name())
                            .userId(user.getId())
                            .totalPrice(order.getTotalPrice())
                            .products(itemInOrderDTOs)
                            .createdDate(order.getCreatedDate())
                            .build();
                })
                .collect(Collectors.toList());
    }

    private String convertImageToBase64(Optional<Image> image) {
        return image.map(img -> Base64.getEncoder().encodeToString(img.getBytes())).orElse(null);
    }
}
