package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.ChatRoom;
import com.example.repository.ChatRoomRepository;
import com.example.repository.MessageRepository;

@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository roomRepository;

    @Autowired
    private MessageRepository messageRepository;

    public List<ChatRoom> getAllRooms() {
        return roomRepository.findAll();
    }

    public ChatRoom getRoomById(Long id) {
        return roomRepository.findById(id).orElse(null);
    }

    public void saveRoom(ChatRoom room) {
        roomRepository.save(room);
    }

    @Transactional
    public void deleteRoomWithMessages(Long id) {
        messageRepository.deleteByRoomId(id);
        roomRepository.deleteById(id);
    }
}