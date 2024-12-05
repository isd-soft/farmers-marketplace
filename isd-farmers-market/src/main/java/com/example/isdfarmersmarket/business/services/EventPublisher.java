package com.example.isdfarmersmarket.business.services;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EventPublisher {

    private ApplicationEventPublisher applicationEventPublisher;

    public void publishEvent(ApplicationEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}