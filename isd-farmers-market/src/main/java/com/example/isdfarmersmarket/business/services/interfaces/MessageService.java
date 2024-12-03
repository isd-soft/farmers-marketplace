package com.example.isdfarmersmarket.business.services.interfaces;

import com.example.isdfarmersmarket.web.commands.SendMessageInConversationCommand;
import com.example.isdfarmersmarket.web.commands.SendMessageInUserPageCommand;
import com.example.isdfarmersmarket.web.dto.MessageDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageService {
    MessageDTO sendMessage(SendMessageInConversationCommand command);

    MessageDTO sendMessage(SendMessageInUserPageCommand command);

    List<MessageDTO> getMessages(Long conversationId, Pageable pageable);
}
