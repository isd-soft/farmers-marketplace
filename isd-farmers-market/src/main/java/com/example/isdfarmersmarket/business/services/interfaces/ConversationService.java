package com.example.isdfarmersmarket.business.services.interfaces;

import com.example.isdfarmersmarket.web.dto.ConversationDTO;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ConversationService {
    ConversationDTO getOrCreateConversation(Long customerId, Long farmerId);
    List<ConversationDTO> getAllConversations(Pageable pageable);
}
