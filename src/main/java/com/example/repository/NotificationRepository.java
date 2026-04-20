package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import com.example.entity.Notification;
import com.example.entity.User;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserOrderByCreatedAtDesc(User user);
    int countByUserAndIsReadFalse(User user);
}
