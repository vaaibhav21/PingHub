package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.entity.MessageReaction;
import com.example.entity.User;
import com.example.repository.MessageReactionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ReactionService {

    @Autowired
    private MessageReactionRepository reactionRepository;

    public void toggleReaction(Message message, User user, String emoji) {
        Optional<MessageReaction> existing = reactionRepository.findByMessageAndUserAndEmoji(message, user, emoji);
        
        if (existing.isPresent()) {
            reactionRepository.delete(existing.get());
        } else {
            MessageReaction r = new MessageReaction();
            r.setMessage(message);
            r.setUser(user);
            r.setEmoji(emoji);
            reactionRepository.save(r);
        }
    }

    public List<MessageReaction> getReactionsForMessage(Message message) {
        return reactionRepository.findByMessage(message);
    }
}
