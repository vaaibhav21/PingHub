package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.UserRoom;
import com.example.repository.UserRoomRepository;

@Service
public class UserRoomService {

    @Autowired
    private UserRoomRepository userRoomRepo;

    public UserRoom save(UserRoom userRoom) {
        return userRoomRepo.save(userRoom);
    }
}