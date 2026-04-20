package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Message;
import com.example.entity.MessageReaction;
import com.example.entity.User;
import java.util.List;
import java.util.Optional;

public interface MessageReactionRepository extends JpaRepository<MessageReaction, Long> {
    List<MessageReaction> findByMessage(Message message);
    Optional<MessageReaction> findByMessageAndUserAndEmoji(Message message, User user, String emoji);
}
