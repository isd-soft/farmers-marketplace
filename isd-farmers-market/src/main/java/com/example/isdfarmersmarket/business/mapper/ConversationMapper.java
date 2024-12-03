package com.example.isdfarmersmarket.business.mapper;

import com.example.isdfarmersmarket.dao.models.Conversation;
import com.example.isdfarmersmarket.web.dto.ConversationDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CompactUserMapper.class, MessageMapper.class})
public interface ConversationMapper {
    ConversationDTO toDto(Conversation conversation);

    Conversation toEntity(ConversationDTO conversationDTO);

    List<ConversationDTO> toDtoList(List<Conversation> conversations);

    List<Conversation> toEntityList(List<ConversationDTO> conversationDTOs);
}
