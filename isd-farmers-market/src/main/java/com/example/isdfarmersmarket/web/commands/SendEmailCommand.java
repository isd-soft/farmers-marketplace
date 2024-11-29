package com.example.isdfarmersmarket.web.commands;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SendEmailCommand {
    String to;
    String subject;
    String htmlContent;
}

