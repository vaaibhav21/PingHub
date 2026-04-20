package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.entity.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}