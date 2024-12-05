package com.example.isdfarmersmarket.business.events;

import com.example.isdfarmersmarket.dao.models.Order;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OrderConfirmedEvent extends ApplicationEvent {

    private final Order order;

    public OrderConfirmedEvent(Object source, Order order) {
        super(source);
        this.order = order;
    }
}