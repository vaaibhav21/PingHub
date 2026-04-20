package com.example.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.entity.User;
import com.example.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/admin-login")
    public String showAdminLoginPage() {
        return "admin-login";
    }

    @PostMapping("/adminLogin")
    public String adminLogin(@RequestParam String username,
                             @RequestParam String password,
                             HttpSession session,
                             Model model) {

        User adminUser = userService.verifyAdminLogin(username, password);

        if (adminUser != null) {
            session.setAttribute("loggedInUser", adminUser);
            return "redirect:/";
        } else {
            model.addAttribute("error", "Invalid Admin Credentials");
            return "admin-login";
        }
    }

    @PostMapping("/sendOtp")
    public String sendOtp(@RequestParam String username,
                          @RequestParam String email,
                          Model model) {

        userService.registerUser(username, email);
        model.addAttribute("email", email);
        return "verify-otp";
    }

    @PostMapping("/verifyOtp")
    public String verifyOtp(@RequestParam String email,
                            @RequestParam String otp,
                            HttpSession session,
                            Model model) {

        boolean isValid = userService.verifyOtp(email, otp);

        if (isValid) {
            User user = userService.findByEmail(email).orElse(null);
            session.setAttribute("loggedInUser", user);
            return "redirect:/";
        } else {
            model.addAttribute("email", email);
            model.addAttribute("error", "Invalid OTP");
            return "verify-otp";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}