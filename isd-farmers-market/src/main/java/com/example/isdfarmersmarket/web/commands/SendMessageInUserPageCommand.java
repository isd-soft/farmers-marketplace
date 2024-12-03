package com.example.isdfarmersmarket.web.commands;

import lombok.Data;

@Data
public class SendMessageInUserPageCommand {
    private Long farmerId;
    private String content;
}
