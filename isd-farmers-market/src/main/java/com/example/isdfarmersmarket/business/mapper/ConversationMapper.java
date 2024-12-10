package com.example.isdfarmersmarket.business.mapper;

import com.example.isdfarmersmarket.dao.models.Conversation;
import com.example.isdfarmersmarket.dao.models.Message;
import com.example.isdfarmersmarket.web.dto.ConversationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CompactUserMapper.class, MessageMapper.class})
public interface ConversationMapper {

    @Mapping(target = "lastMessage", source = "conversation", qualifiedByName = "getLastMessage")
    ConversationDTO toDto(Conversation conversation);

    Conversation toEntity(ConversationDTO conversationDTO);

    List<ConversationDTO> toDtoList(List<Conversation> conversations);

    List<Conversation> toEntityList(List<ConversationDTO> conversationDTOs);

    @Named("getLastMessage")
    default String getLastMessage(Conversation conversation) {
        if (conversation != null && !conversation.getMessages().isEmpty()) {
            Message lastMessage = conversation.getMessages().getLast();
            return lastMessage.getContent();
        }
        return null;
    }
}
