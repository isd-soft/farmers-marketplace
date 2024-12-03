package com.example.isdfarmersmarket.business.services;

import com.example.isdfarmersmarket.business.mapper.MessageMapper;
import com.example.isdfarmersmarket.business.security.JwtPrincipal;
import com.example.isdfarmersmarket.business.services.interfaces.ConversationService;
import com.example.isdfarmersmarket.business.services.interfaces.MessageService;
import com.example.isdfarmersmarket.business.utils.SecurityUtils;
import com.example.isdfarmersmarket.dao.models.Conversation;
import com.example.isdfarmersmarket.dao.models.Message;
import com.example.isdfarmersmarket.dao.models.User;
import com.example.isdfarmersmarket.dao.repositories.ConversationRepository;
import com.example.isdfarmersmarket.dao.repositories.MessageRepository;
import com.example.isdfarmersmarket.dao.repositories.UserRepository;
import com.example.isdfarmersmarket.web.commands.SendMessageInConversationCommand;
import com.example.isdfarmersmarket.web.commands.SendMessageInUserPageCommand;
import com.example.isdfarmersmarket.web.dto.ConversationDTO;
import com.example.isdfarmersmarket.web.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final ConversationService conversationService;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final MessageMapper messageMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    @Override
    public MessageDTO sendMessage(SendMessageInConversationCommand command) {
        JwtPrincipal principal = SecurityUtils.getPrincipal();
        if (command.getContent() == null || command.getContent().isBlank()) {
            throw new IllegalArgumentException("Message content cannot be empty.");
        }

        User sender = userRepository.findById(principal.getId()).orElseThrow();
        Conversation conversation = conversationRepository.findById(command.getConversationId()).orElseThrow();

        Message message = Message.builder()
                .sender(sender)
                .conversation(conversation)
                .content(command.getContent())
                .build();

        var newMessage = messageRepository.save(message);
        messagingTemplate.convertAndSend("/topic/messages/" + conversation.getId(), messageMapper.toDto(newMessage));
        return messageMapper.toDto(newMessage);
    }
    @Transactional
    @Override
    public MessageDTO sendMessage(SendMessageInUserPageCommand command) {
        JwtPrincipal principal = SecurityUtils.getPrincipal();
        if (command.getContent() == null || command.getContent().isBlank()) {
            throw new IllegalArgumentException("Message content cannot be empty.");
        }

        ConversationDTO conversation = conversationService.getOrCreateConversation(principal.getId(), command.getFarmerId());

        SendMessageInConversationCommand newCommand =
                new SendMessageInConversationCommand(conversation.getId(), command.getContent());
        return sendMessage(newCommand);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MessageDTO> getMessages(Long conversationId, Pageable pageable) {
        Conversation conversation = conversationRepository.findById(conversationId).orElseThrow();
        var messages = messageRepository.findByConversation(conversation, pageable);
        return messageMapper.toDtoList(messages);
    }
}
