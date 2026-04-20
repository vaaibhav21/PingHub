package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.entity.ChatRoom;
import com.example.entity.User;
import com.example.service.ChatRoomService;
import com.example.service.MessageService;
import com.example.service.ChatRoomService;
import com.example.service.UserService;
import com.example.service.FriendshipService;
import com.example.service.NotificationService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ChatRoomController {

    @Autowired
    private ChatRoomService roomService;

    @Autowired
    private UserService userService;

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/")
    public String home(HttpSession session, Model model) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("rooms", roomService.getAllRooms());
        model.addAttribute("user", loggedInUser);
        
        // Pass system users (excluding self and already accepted friends ideally, but for now just exclude self)
        model.addAttribute("otherUsers", userService.getAllOtherUsers(loggedInUser.getId()));

        model.addAttribute("friends", friendshipService.getAcceptedFriends(loggedInUser));
        model.addAttribute("friendRequests", friendshipService.getPendingRequests(loggedInUser));
        model.addAttribute("notifications", notificationService.getUserNotifications(loggedInUser));
        model.addAttribute("unreadCount", notificationService.getUnreadCount(loggedInUser));

        return "index";
    }

    @PostMapping("/addRoom")
    public String addRoom(@RequestParam String roomName, HttpSession session) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        if (!"ADMIN".equals(loggedInUser.getRole())) {
            return "redirect:/"; // unauthorized action
        }

        ChatRoom room = new ChatRoom();
        room.setRoomName(roomName);
        room.setAccessKey(java.util.UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        room.getAccessedUsers().add(loggedInUser);
        roomService.saveRoom(room);

        return "redirect:/";
    }

    @PostMapping("/deleteRoom/{id}")
    public String deleteRoom(@PathVariable Long id, HttpSession session) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        if (!"ADMIN".equals(loggedInUser.getRole())) {
            return "redirect:/"; // unauthorized action
        }

        roomService.deleteRoomWithMessages(id);

        return "redirect:/";
    }
}