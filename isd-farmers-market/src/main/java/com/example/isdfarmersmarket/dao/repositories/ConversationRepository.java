package com.example.isdfarmersmarket.dao.repositories;

import com.example.isdfarmersmarket.dao.models.Conversation;
import com.example.isdfarmersmarket.dao.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Optional<Conversation> findByCustomerAndFarmer(User customer, User farmer);
    List<Conversation> findByCustomer(User customer, Pageable pageable);
    List<Conversation> findByFarmer(User farmer, Pageable pageable);
}
