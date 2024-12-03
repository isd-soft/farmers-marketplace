package com.example.isdfarmersmarket.web.controllers;

import com.example.isdfarmersmarket.business.services.interfaces.ConversationService;
import com.example.isdfarmersmarket.business.services.interfaces.MessageService;
import com.example.isdfarmersmarket.web.commands.SendMessageInConversationCommand;
import com.example.isdfarmersmarket.web.commands.SendMessageInUserPageCommand;
import com.example.isdfarmersmarket.web.dto.ConversationDTO;
import com.example.isdfarmersmarket.web.dto.MessageDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import java.util.List;

    @RestController
    @RequiredArgsConstructor
    @RequestMapping("/messaging")
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public class MessagingController {
        MessageService messageService;
        ConversationService conversationService;

        @PostMapping("/conversations")
        public MessageDTO sendMessageToConversation(@RequestBody SendMessageInConversationCommand command) {
            return messageService.sendMessage(command);
        }
        @PostMapping("/user")
        public MessageDTO sendMessage(@RequestBody SendMessageInUserPageCommand command) {
            return messageService.sendMessage(command);
        }

        @GetMapping("/conversations/{conversationId}")
        public List<MessageDTO> getMessages(@PathVariable Long conversationId, Pageable pageable) {
            return messageService.getMessages(conversationId, pageable);
        }

        @GetMapping("/conversations")
        public List<ConversationDTO> getConversations(Pageable pageable) {
            return conversationService.getAllConversations(pageable);
        }

    }
