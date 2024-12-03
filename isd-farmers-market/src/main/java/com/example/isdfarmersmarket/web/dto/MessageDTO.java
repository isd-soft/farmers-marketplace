package com.example.isdfarmersmarket.web.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageDTO {
    private Long id;
    private Long conversationId;
    private CompactUserDTO sender;
    private String content;
    private LocalDateTime sentAt;
}
