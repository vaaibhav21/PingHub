package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.entity.User;
import com.example.service.UserService;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/addUser")
    public String addUser(@RequestParam String username) {

        System.out.println("User received: " + username); // DEBUG

        User user = new User();
        user.setUsername(username);

        userService.saveUser(user);

        return "redirect:/";
    }
}