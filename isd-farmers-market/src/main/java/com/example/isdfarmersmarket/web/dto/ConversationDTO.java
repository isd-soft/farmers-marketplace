package com.example.isdfarmersmarket.web.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConversationDTO {
    private Long id;
    private CompactUserDTO customer;
    private CompactUserDTO farmer;
    private LocalDateTime createdAt;
    private String lastMessage;
}
