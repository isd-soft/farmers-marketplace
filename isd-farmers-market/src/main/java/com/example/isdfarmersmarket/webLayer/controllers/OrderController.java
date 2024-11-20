package com.example.isdfarmersmarket.controllers;

import com.example.isdfarmersmarket.models.Order;
import com.example.isdfarmersmarket.repositories.UserRepository;
import com.example.isdfarmersmarket.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.Authenticator;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {
    @Autowired
    private OrderService service;

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/addOrder")
    public Order addOrder(@RequestBody Order order, Authentication authentication) {
        return service.saveOrder(order,authentication.getName());
    }

    @GetMapping("/orders")
    public List<Order> findAllOrders() {
        return service.getOrders();
    }

    @GetMapping("/orderById/{id}")
    public Order findOrderById(@PathVariable Long id) {
        return service.getOrderById(id);
    }

    @PutMapping("/update")
    public Order updateOrder(@RequestBody Order order) {
        return service.updateOrder(order);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Long id) {
        return service.deleteOrder(id);
    }
}

