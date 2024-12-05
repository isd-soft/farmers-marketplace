package com.example.isdfarmersmarket.business.listeners;

import com.example.isdfarmersmarket.business.events.OrderConfirmedEvent;
import com.example.isdfarmersmarket.business.events.OrderPlacedEvent;
import com.example.isdfarmersmarket.business.services.EmailSenderService;
import com.example.isdfarmersmarket.dao.models.Order;
import com.example.isdfarmersmarket.web.commands.SendEmailCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class OrderPlacedListener {
    private final SpringTemplateEngine templateEngine;
    private final EmailSenderService emailSenderService;

    @Async
    @EventListener
    public void handleOrderConfirmedEvent(OrderPlacedEvent event) {
        Order order = event.getOrder();

        String formattedDate = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(order.getCreatedDate());

        Context context = new Context();
        context.setVariable("firstName", order.getFarmer().getFirstName());
        context.setVariable("lastName", order.getFarmer().getLastName());
        context.setVariable("orderId", order.getId());
        context.setVariable("orderDate", formattedDate);
        context.setVariable("totalPrice", order.getTotalPrice());
        context.setVariable("items", order.getItemsInOrder());

        String htmlContent = templateEngine.process("order-placed", context);

        SendEmailCommand emailCommand = new SendEmailCommand(
                order.getCustomer().getEmail(),
                "Order Confirmation - #" + order.getId(),
                htmlContent
        );
        emailSenderService.sendEmail(emailCommand);
    }
}