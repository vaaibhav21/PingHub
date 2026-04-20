package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.entity.User;
import com.example.service.FriendshipService;
import com.example.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/friends")
public class FriendshipController {

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private UserService userService;

    @PostMapping("/add/{id}")
    public String addFriend(@PathVariable Long id, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/login";

        User addressee = userService.getUser(id);
        if (addressee != null) {
            friendshipService.sendRequest(loggedInUser, addressee);
        }
        return "redirect:/";
    }

    @PostMapping("/accept/{friendshipId}")
    public String acceptFriend(@PathVariable Long friendshipId, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            friendshipService.acceptRequest(friendshipId, loggedInUser);
        }
        return "redirect:/";
    }
}
