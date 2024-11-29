package com.example.isdfarmersmarket.business.services;

import com.example.isdfarmersmarket.dao.models.ItemInOrder;
import com.example.isdfarmersmarket.dao.models.Order;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

@Service
public class HtmlEmailService {

    public String generateCustomerOrderConfirmed(Order order) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = order.getCreatedDate().format(formatter);
        StringBuilder html = new StringBuilder();

        // Start of HTML structure
        html.append("<!DOCTYPE html><html lang=\"en\"><head>")
                .append("<meta charset=\"UTF-8\">")
                .append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">")
                .append("<title>Order Confirmation</title>")
                .append("<style>")
                .append("body { font-family: Arial, sans-serif; margin: 0; background-color: #f4f4f9; padding: 5px; }")
                .append(".email-container { max-width: 600px; margin: 0 auto; background: #ffffff; overflow: hidden; }")
                .append(".email-header { background-color: #179739; padding: 5px; text-align: center; }")
                .append(".email-header h1 { color: #ffffff; font-size: 20px; margin: 5px 0 0; font-weight: bold; }")
                .append(".email-body { color: #334155; padding: 5px;  font-size: 18px; }")
                .append(".email-body h2 { color: #334155; font-size: 20px; margin-bottom: 5px; font-weight: bold; }")
                .append(".email-body h3 { color: #334155; font-size: 20px; margin-bottom: 5px; font-weight: bold; }")
                .append(".email-body ul { color: #334155;  }")
                .append(".email-body p { color: #334155; }")
                .append(".email-body ul li { color: #334155; font-size: 18px;  }")
                .append(".email-footer { background-color: #179739; color: #ffffff; text-align: center; padding: 5px; font-size: 16px; }")
                .append("</style>")
                .append("</head><body>")
                .append("<div class=\"email-container\">");

        // Header Section
        html.append("<div class=\"email-header\">")
                .append("<h1>Your Order Confirmation</h1>")
                .append("</div>");

        // Body Section
        html.append("<div class=\"email-body\">")
                .append("<h2>Thank you for your order, ").append(order.getUser().getFirstName()).append(" ")
                .append(order.getUser().getLastName()).append("!</h2>")
                .append("<p>Your order has been confirmed and is being processed. Below are the details:</p>")
                .append("<ul>")
                .append("<li><strong>Order Number:</strong> ").append(order.getId()).append("</li>")
                .append("<li><strong>Order Date:</strong> ").append(formattedDate).append("</li>")
                .append("<li><strong>Total Price:</strong> $")
                .append(String.format("%.2f", order.getTotalPrice()))
                .append("</li>")
                .append("</ul>");

        html.append("<h3>Products in Your Order:</h3>")
                .append("<ul>");

        for (ItemInOrder item : order.getProducts()) {
            BigDecimal pricePerUnit = item.getPricePerUnit();
            BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
            BigDecimal totalPrice = pricePerUnit.multiply(quantity);

            DecimalFormat df = new DecimalFormat("0.00");
            String formattedPrice = df.format(pricePerUnit);
            String formattedTotal = df.format(totalPrice);

            html.append("<li>")
                    .append(item.getProduct().getTitle())
                    .append(" - Quantity: ")
                    .append(item.getQuantity())
                    .append(" - Price: $")
                    .append(formattedPrice)
                    .append(" - Total: $")
                    .append(formattedTotal)
                    .append("</li>");
        }

        html.append("</ul>")
                .append("<p>Thank you for shopping with us! You will receive further updates about your order as it is processed.</p>")
                .append("<p>Best regards,<br>Farmers Market</p>")
                .append("</div>");

        // Footer Section
        html.append("<div class=\"email-footer\">")
                .append("<p>&copy; 2024 Farmers Market. All rights reserved.</p>")
                .append("</div>");

        // Closing HTML tags
        html.append("</div></body></html>");

        return html.toString();
    }
}