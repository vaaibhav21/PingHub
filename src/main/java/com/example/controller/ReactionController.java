package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.entity.Message;
import com.example.entity.User;
import com.example.service.MessageService;
import com.example.service.ReactionService;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/reaction")
public class ReactionController {

    @Autowired
    private ReactionService reactionService;

    @Autowired
    private MessageService messageService;

    @PostMapping("/toggle")
    public String toggleReaction(@RequestParam Long messageId, 
                                 @RequestParam String emoji, 
                                 HttpSession session,
                                 HttpServletRequest request) {
        
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/login";

        Message message = messageService.getMessage(messageId);
        if (message != null) {
            reactionService.toggleReaction(message, loggedInUser, emoji);
        }

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }
}
