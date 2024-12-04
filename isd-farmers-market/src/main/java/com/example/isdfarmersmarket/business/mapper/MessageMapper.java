package com.example.isdfarmersmarket.business.mapper;

import com.example.isdfarmersmarket.dao.models.Message;
import com.example.isdfarmersmarket.web.dto.MessageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CompactUserMapper.class, ConversationMapper.class})
public interface MessageMapper {
    @Mapping(target = "conversationId", source = "conversation.id")
    MessageDTO toDto(Message message);

    Message toEntity(MessageDTO messageDTO);

    List<MessageDTO> toDtoList(List<Message> messages);

    List<Message> toEntityList(List<MessageDTO> messageDTOs);
}
