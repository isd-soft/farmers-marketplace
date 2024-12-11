package com.example.isdfarmersmarket.business.mapper;

import com.example.isdfarmersmarket.dao.models.Conversation;
import com.example.isdfarmersmarket.dao.models.Message;
import com.example.isdfarmersmarket.dao.repositories.MessageRepository;
import com.example.isdfarmersmarket.web.dto.ConversationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CompactUserMapper.class, MessageMapper.class})
public abstract class ConversationMapper {

    @Autowired
    protected MessageRepository messageRepository;

    @Mapping(target = "lastMessage", source = "conversation", qualifiedByName = "getLastMessage")
    public abstract ConversationDTO toDto(Conversation conversation);

    public abstract Conversation toEntity(ConversationDTO conversationDTO);

    public abstract List<ConversationDTO> toDtoList(List<Conversation> conversations);

    public abstract List<Conversation> toEntityList(List<ConversationDTO> conversationDTOs);

    @Named("getLastMessage")
    protected String getLastMessage(Conversation conversation) {
        if (conversation != null) {
            return messageRepository.findTopByConversationOrderBySentAtDesc(conversation)
                    .map(Message::getContent)
                    .orElse(null);
        }
        return null;
    }
}