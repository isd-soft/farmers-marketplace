package com.example.isdfarmersmarket.business.services;

import com.example.isdfarmersmarket.business.mapper.ConversationMapper;
import com.example.isdfarmersmarket.business.security.JwtPrincipal;
import com.example.isdfarmersmarket.business.services.interfaces.ConversationService;
import com.example.isdfarmersmarket.business.utils.SecurityUtils;
import com.example.isdfarmersmarket.dao.enums.ERole;
import com.example.isdfarmersmarket.dao.models.Conversation;
import com.example.isdfarmersmarket.dao.models.User;
import com.example.isdfarmersmarket.dao.repositories.ConversationRepository;
import com.example.isdfarmersmarket.dao.repositories.UserRepository;
import com.example.isdfarmersmarket.web.dto.ConversationDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConversationServiceImpl implements ConversationService {
    ConversationRepository conversationRepository;
    UserRepository userRepository;
    ConversationMapper conversationMapper;

    @Transactional
    public ConversationDTO getOrCreateConversation(Long customerId, Long farmerId) {
        User customer = userRepository.findById(customerId).orElseThrow();
        User farmer = userRepository.findById(farmerId).orElseThrow();


        if (customer.getRoles().stream().anyMatch(role -> role.getRole().equals(ERole.FARMER)) ||
                farmer.getRoles().stream().noneMatch(role -> role.getRole().equals(ERole.FARMER))) {
            throw new IllegalStateException("Only customers can initiate conversations with farmers.");
        }

        var conversation = conversationRepository.findByCustomerAndFarmer(customer, farmer)
                .orElseGet(() -> {
                    var newConversation = Conversation.builder()
                            .customer(customer)
                            .farmer(farmer)
                            .build();
                    return conversationRepository.save(newConversation);
                });
        return conversationMapper.toDto(conversation);
    }
    private List<ConversationDTO> getConversationsForCustomer(Long customerId, Pageable pageable) {
        User customer = userRepository.findById(customerId).orElseThrow();
        List<Conversation> conversations = conversationRepository.findByCustomer(customer, pageable);
        return conversationMapper.toDtoList(conversations);
    }
    private List<ConversationDTO> getConversationsForFarmer(Long farmerId, Pageable pageable) {
        User customer = userRepository.findById(farmerId).orElseThrow();
        List<Conversation> convos = conversationRepository.findByFarmer(customer, pageable);
        return conversationMapper.toDtoList(convos);
    }
    @Transactional(readOnly = true)
    public List<ConversationDTO> getAllConversations(Pageable pageable) {
        JwtPrincipal principal = SecurityUtils.getPrincipal();
        if(principal.getRoles().contains(ERole.FARMER))
            return getConversationsForFarmer(principal.getId(), pageable);
        return getConversationsForCustomer(principal.getId(), pageable);
    }

}
