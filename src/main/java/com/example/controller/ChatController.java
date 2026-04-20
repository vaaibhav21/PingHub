package com.example.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.entity.ChatRoom;
import com.example.entity.Message;
import com.example.entity.User;
import com.example.service.ChatRoomService;
import com.example.service.FileStorageService;
import com.example.service.MessageService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ChatController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private ChatRoomService roomService;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/room/{id}")
    public String openRoom(@PathVariable Long id, Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        ChatRoom room = roomService.getRoomById(id);

        if (room == null) {
            return "redirect:/";
        }

        if (!"general chat".equalsIgnoreCase(room.getRoomName()) 
                && !"ADMIN".equals(loggedInUser.getRole()) 
                && !room.getAccessedUsers().contains(loggedInUser)) {
            model.addAttribute("roomId", id);
            model.addAttribute("roomName", room.getRoomName());
            return "enter-key";
        }

        messageService.markAllMessagesAsDelivered(id);

        model.addAttribute("messages", messageService.getMessagesByRoom(id));
        model.addAttribute("roomId", id);
        model.addAttribute("roomName", room.getRoomName());
        model.addAttribute("loggedInUser", loggedInUser);

        return "chat";
    }

    @PostMapping("/room/join")
    public String joinRoom(@RequestParam Long roomId,
                           @RequestParam String accessKey,
                           Model model,
                           HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/login";

        ChatRoom room = roomService.getRoomById(roomId);
        if (room == null) return "redirect:/";

        if (accessKey != null && accessKey.equals(room.getAccessKey())) {
            User managedUser = userService.getUser(loggedInUser.getId());
            room.getAccessedUsers().add(managedUser);
            roomService.saveRoom(room);
            return "redirect:/room/" + roomId;
        }

        model.addAttribute("roomId", roomId);
        model.addAttribute("roomName", room.getRoomName());
        model.addAttribute("error", "Invalid access key. Please try again.");
        return "enter-key";
    }

    @PostMapping("/send")
    public String sendMessage(@RequestParam String content,
                              @RequestParam Long roomId,
                              HttpSession session) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        if (content == null || content.trim().isEmpty()) {
            return "redirect:/room/" + roomId;
        }

        ChatRoom room = roomService.getRoomById(roomId);

        if (room == null) {
            return "redirect:/";
        }

        Message msg = new Message();
        msg.setContent(content.trim());
        msg.setRoom(room);
        msg.setUser(loggedInUser);
        msg.setSentAt(LocalDateTime.now());
        msg.setDeleted(false);
        msg.setMessageType("TEXT");
        msg.setStatus("SENT");

        messageService.saveMessage(msg);

        return "redirect:/room/" + roomId;
    }

    @PostMapping("/send-media")
    @ResponseBody
    public Map<String, Object> sendMediaMessage(@RequestParam("file") MultipartFile file,
                                                @RequestParam Long roomId,
                                                HttpSession session) throws IOException {

        Map<String, Object> response = new HashMap<>();

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            response.put("success", false);
            response.put("message", "User not logged in");
            return response;
        }

        if (file == null || file.isEmpty()) {
            response.put("success", false);
            response.put("message", "No file selected");
            return response;
        }

        ChatRoom room = roomService.getRoomById(roomId);

        if (room == null) {
            response.put("success", false);
            response.put("message", "Room not found");
            return response;
        }

        String savedFileUrl = fileStorageService.saveFile(file);

        Message msg = new Message();
        msg.setRoom(room);
        msg.setUser(loggedInUser);
        msg.setSentAt(LocalDateTime.now());
        msg.setDeleted(false);

        Message savedMessage = messageService.createMediaMessage(
                msg,
                file.getOriginalFilename(),
                savedFileUrl,
                file.getContentType()
        );

        response.put("success", true);
        response.put("id", savedMessage.getId());
        response.put("roomId", roomId);
        response.put("username", loggedInUser.getUsername());
        response.put("messageType", savedMessage.getMessageType());
        response.put("fileName", savedMessage.getFileName());
        response.put("fileUrl", savedMessage.getFileUrl());
        response.put("contentType", savedMessage.getContentType());
        response.put("status", savedMessage.getStatus());
        response.put("deleted", savedMessage.isDeleted());
        response.put("sentAt",
                savedMessage.getSentAt() != null
                        ? DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm a").format(savedMessage.getSentAt())
                        : "");

        return response;
    }

    @PostMapping("/message/delete/{messageId}")
    public String deleteMessage(@PathVariable Long messageId,
                                @RequestParam Long roomId,
                                HttpSession session) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        Message message = messageService.getMessageById(messageId);

        if (message != null
                && message.getUser() != null
                && message.getUser().getId().equals(loggedInUser.getId())) {
            messageService.softDeleteMessage(message);
        }

        return "redirect:/room/" + roomId;
    }

    @PostMapping("/message/permanent-delete/{messageId}")
    public String permanentlyDeleteMessage(@PathVariable Long messageId,
                                           @RequestParam Long roomId,
                                           HttpSession session) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        Message message = messageService.getMessageById(messageId);

        if (message != null
                && message.getUser() != null
                && message.getUser().getId().equals(loggedInUser.getId())) {
            messageService.permanentlyDeleteMessage(messageId);
        }

        return "redirect:/room/" + roomId;
    }

    @PostMapping("/message/read/{messageId}")
    public String markMessageAsRead(@PathVariable Long messageId,
                                    @RequestParam Long roomId,
                                    HttpSession session) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        messageService.markMessageAsRead(messageId);

        return "redirect:/room/" + roomId;
    }

    @Autowired
    private com.example.service.UserService userService;

    @GetMapping("/direct/{id}")
    public String openDirectChat(@PathVariable Long id, Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/login";

        User recipient = userService.getUser(id);
        if (recipient == null) return "redirect:/";

        model.addAttribute("messages", messageService.getDirectMessages(loggedInUser, recipient));
        model.addAttribute("recipient", recipient);
        model.addAttribute("loggedInUser", loggedInUser);
        
        return "direct-chat";
    }

    @PostMapping("/send-direct")
    public String sendDirectMessage(@RequestParam String content,
                                    @RequestParam Long recipientId,
                                    HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/login";

        if (content == null || content.trim().isEmpty()) return "redirect:/direct/" + recipientId;

        User recipient = userService.getUser(recipientId);
        if (recipient == null) return "redirect:/";

        Message msg = new Message();
        msg.setContent(content.trim());
        msg.setRecipient(recipient);
        msg.setUser(loggedInUser);
        msg.setSentAt(LocalDateTime.now());
        msg.setDeleted(false);
        msg.setMessageType("TEXT");
        msg.setStatus("SENT");

        messageService.saveMessage(msg);
        return "redirect:/direct/" + recipientId;
    }

    @PostMapping("/send-media-direct")
    @ResponseBody
    public Map<String, Object> sendMediaDirect(@RequestParam("file") MultipartFile file,
                                               @RequestParam Long recipientId,
                                               HttpSession session) throws IOException {

        Map<String, Object> response = new HashMap<>();
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            response.put("success", false);
            return response;
        }

        User recipient = userService.getUser(recipientId);
        if (recipient == null || file == null || file.isEmpty()) {
            response.put("success", false);
            return response;
        }

        String savedFileUrl = fileStorageService.saveFile(file);

        Message msg = new Message();
        msg.setRecipient(recipient);
        msg.setUser(loggedInUser);
        msg.setSentAt(LocalDateTime.now());
        msg.setDeleted(false);

        Message savedMessage = messageService.createMediaMessage(
                msg,
                file.getOriginalFilename(),
                savedFileUrl,
                file.getContentType()
        );

        response.put("success", true);
        response.put("id", savedMessage.getId());
        response.put("recipientId", recipientId);
        response.put("username", loggedInUser.getUsername());
        response.put("messageType", savedMessage.getMessageType());
        response.put("fileName", savedMessage.getFileName());
        response.put("fileUrl", savedMessage.getFileUrl());
        response.put("contentType", savedMessage.getContentType());
        response.put("status", savedMessage.getStatus());
        response.put("sentAt", savedMessage.getSentAt() != null 
                ? DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm a").format(savedMessage.getSentAt()) : "");

        return response;
    }
}