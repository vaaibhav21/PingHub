package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.entity.Message;
import com.example.entity.User;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByRoomId(Long roomId);

    List<Message> findByRoomIdOrderBySentAtAsc(Long roomId);

    void deleteByRoomId(Long roomId);

    @Query("SELECT m FROM Message m WHERE (m.user = :user1 AND m.recipient = :user2) OR (m.user = :user2 AND m.recipient = :user1) ORDER BY m.sentAt ASC")
    List<Message> findDirectMessages(@Param("user1") User user1, @Param("user2") User user2);
}