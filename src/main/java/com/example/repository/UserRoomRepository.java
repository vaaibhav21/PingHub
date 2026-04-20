package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.entity.UserRoom;

public interface UserRoomRepository extends JpaRepository<UserRoom, Long> {
}