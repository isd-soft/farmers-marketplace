package com.example.isdfarmersmarket.services;

import com.example.isdfarmersmarket.models.Order;
import com.example.isdfarmersmarket.models.User;
import com.example.isdfarmersmarket.repositories.OrderRepository;
import com.example.isdfarmersmarket.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    public final UserRepository userRepository;

    public Order saveOrder(Order order, String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        order.setUser(user);
        return orderRepository.save(order);
    }
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }


    public String deleteOrder(Long id) {
        orderRepository.deleteById(id);
        return "product removed!" + id;
    }

    public Order updateOrder(Order order) {
        Order existingOrder = orderRepository.findById(order.getId()).orElse(null);
        assert existingOrder != null;
        existingOrder.setId(order.getId());
        existingOrder.setUser(order.getUser());
        existingOrder.setTotalPrice(order.getTotalPrice());
        existingOrder.setProducts(order.getProducts());
        existingOrder.setTotalPrice(order.getTotalPrice());
        existingOrder.setCreatedDate(order.getCreatedDate());
        return orderRepository.save(existingOrder);
    }
}

