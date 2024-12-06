package com.example.isdfarmersmarket.business.listeners;

import com.example.isdfarmersmarket.business.events.OrderPlacedEvent;
import com.example.isdfarmersmarket.business.services.EmailSenderService;
import com.example.isdfarmersmarket.dao.models.ItemInOrder;
import com.example.isdfarmersmarket.dao.models.Order;
import com.example.isdfarmersmarket.web.commands.SendEmailCommand;
import com.example.isdfarmersmarket.web.dto.ProductInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderPlacedListener {
    private final SpringTemplateEngine templateEngine;
    private final EmailSenderService emailSenderService;

    @EventListener
    public void handleOrderPlacedEvent(OrderPlacedEvent event) {
        Order order = event.getOrder();
        List<ItemInOrder> items = event.getItems();

        String formattedDate = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(order.getCreatedDate());

        Context context = new Context();
        context.setVariable("firstName", order.getFarmer().getFirstName());
        context.setVariable("lastName", order.getFarmer().getLastName());
        context.setVariable("orderId", order.getId());
        context.setVariable("orderDate", formattedDate);
        context.setVariable("totalPrice", order.getTotalPrice());

        List<ProductInfoDTO> productInfoList = new ArrayList<>();
        for (ItemInOrder item : items) {

            ProductInfoDTO productInfo = new ProductInfoDTO(
                    item.getProduct().getTitle(),
                    item.getQuantity(),
                    item.getPricePerUnit()
            );
            productInfoList.add(productInfo);
        }

        context.setVariable("productItems", productInfoList);

        String htmlContent = templateEngine.process("order-placed", context);

        SendEmailCommand emailCommand = new SendEmailCommand(
                order.getFarmer().getEmail(),
                "Order Confirmation - #" + order.getId(),
                htmlContent
        );
        emailSenderService.sendEmail(emailCommand);
    }
}