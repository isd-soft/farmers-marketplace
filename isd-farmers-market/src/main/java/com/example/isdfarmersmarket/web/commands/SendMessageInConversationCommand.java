package com.example.isdfarmersmarket.web.commands;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SendMessageInConversationCommand {
    private Long conversationId;
    private String content;

}
