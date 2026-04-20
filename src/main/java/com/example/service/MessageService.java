package com.example.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Message;
import com.example.entity.User;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    // ================= GET MESSAGES (FIXED ORDERING) =================

    public List<Message> getMessagesByRoom(Long roomId) {
        return messageRepository.findByRoomIdOrderBySentAtAsc(roomId);
    }

    public List<Message> getDirectMessages(User user1, User user2) {
        return messageRepository.findDirectMessages(user1, user2);
    }

    public Message getMessage(Long id) {
        return messageRepository.findById(id).orElse(null);
    }

    // ================= SAVE MESSAGE =================

    public void saveMessage(Message message) {
        if (message.getSentAt() == null) {
            message.setSentAt(LocalDateTime.now());
        }

        if (message.getMessageType() == null) {
            message.setMessageType("TEXT");
        }

        if (message.getStatus() == null) {
            message.setStatus("SENT");
        }

        messageRepository.save(message);
    }

    // ================= DELETE =================

    @Transactional
    public void deleteMessagesByRoomId(Long roomId) {
        messageRepository.deleteByRoomId(roomId);
    }

    public Message getMessageById(Long id) {
        Optional<Message> optionalMessage = messageRepository.findById(id);
        return optionalMessage.orElse(null);
    }

    public void softDeleteMessage(Message message) {
        if (message != null) {
            message.setContent("This message was deleted");
            message.setDeleted(true);

            // clear media safely
            message.setFileName(null);
            message.setFileUrl(null);
            message.setContentType(null);
            message.setMessageType("TEXT");

            messageRepository.save(message);
        }
    }

    public void permanentlyDeleteMessage(Long messageId) {
        messageRepository.deleteById(messageId);
    }

    // ================= MEDIA SUPPORT =================

    public Message createTextMessage(Message message) {
        message.setMessageType("TEXT");
        message.setSentAt(LocalDateTime.now());
        message.setStatus("SENT");
        return messageRepository.save(message);
    }

    public Message createMediaMessage(Message message,
                                      String fileName,
                                      String fileUrl,
                                      String contentType) {

        message.setFileName(fileName);
        message.setFileUrl(fileUrl);
        message.setContentType(contentType);
        message.setSentAt(LocalDateTime.now());
        message.setStatus("SENT");

        if (contentType != null && contentType.startsWith("image/")) {
            message.setMessageType("IMAGE");
        } else if (contentType != null && contentType.startsWith("audio/")) {
            message.setMessageType("AUDIO");
        } else {
            message.setMessageType("FILE");
        }

        return messageRepository.save(message);
    }

    // ================= MESSAGE STATUS =================

    public void markMessageAsDelivered(Long messageId) {
        Message message = getMessageById(messageId);

        if (message != null && !"READ".equals(message.getStatus())) {
            message.setStatus("DELIVERED");
            messageRepository.save(message);
        }
    }

    public void markMessageAsRead(Long messageId) {
        Message message = getMessageById(messageId);

        if (message != null) {
            message.setStatus("READ");
            messageRepository.save(message);
        }
    }

    public void markAllMessagesAsDelivered(Long roomId) {
        List<Message> messages = messageRepository.findByRoomIdOrderBySentAtAsc(roomId);

        for (Message message : messages) {
            if (!"READ".equals(message.getStatus())) {
                message.setStatus("DELIVERED");
            }
        }

        messageRepository.saveAll(messages);
    }

    public void markAllMessagesAsRead(Long roomId) {
        List<Message> messages = messageRepository.findByRoomIdOrderBySentAtAsc(roomId);

        for (Message message : messages) {
            message.setStatus("READ");
        }

        messageRepository.saveAll(messages);
    }
}