package com.example.isdfarmersmarket.business.listeners;

import com.example.isdfarmersmarket.business.services.EmailSenderService;
import com.example.isdfarmersmarket.business.services.HtmlEmailService;
import com.example.isdfarmersmarket.dao.models.Order;
import com.example.isdfarmersmarket.web.commands.SendEmailCommand;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@AllArgsConstructor
@Component
public class OrderConfirmedListener {
    private HtmlEmailService emailService;
    private EmailSenderService emailSenderService;

    @EventListener
    public void handleOnConfirmedOrder(Order order) {
        String htmlMessage = emailService.generateCustomerOrderConfirmed(order);

        SendEmailCommand sendEmailCommand = new SendEmailCommand(
                order.getUser().getEmail(),
                "Order Confirmation - #" + order.getId(),
                htmlMessage
        );
        emailSenderService.sendEmail(sendEmailCommand);
    }
}