package com.example.isdfarmersmarket.business.events;

import com.example.isdfarmersmarket.dao.models.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class RegistrationCompleteEvent extends ApplicationEvent {
    private String appUrl;
    private User user;

    public RegistrationCompleteEvent(User user, String appUrl) {
        super(user);
        this.user = user;
        this.appUrl = appUrl;
    }
}
