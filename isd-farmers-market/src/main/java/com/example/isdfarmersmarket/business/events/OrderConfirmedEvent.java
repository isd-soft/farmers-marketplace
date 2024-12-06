package com.example.isdfarmersmarket.business.events;

import com.example.isdfarmersmarket.dao.models.ItemInOrder;
import com.example.isdfarmersmarket.dao.models.Order;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class OrderConfirmedEvent extends ApplicationEvent {

    private final Order order;
    private final List<ItemInOrder> items;

    public OrderConfirmedEvent(Object source, Order order, List<ItemInOrder> items) {
        super(source);
        this.order = order;
        this.items = items;
    }
}