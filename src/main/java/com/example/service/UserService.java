package com.example.service;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.User;
import com.example.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @jakarta.annotation.PostConstruct
    public void initAdmin() {
        Optional<User> adminOpt = userRepository.findByEmail("admin@admin.com");
        if (adminOpt.isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@admin.com");
            admin.setPassword("admin");
            admin.setRole("ADMIN");
            admin.setVerified(true);
            userRepository.save(admin);
        }
    }

    public User verifyAdminLogin(String username, String password) {
        // Find by simple iterative search since it's just the admin.
        return userRepository.findAll().stream()
            .filter(u -> "ADMIN".equals(u.getRole()) 
                    && username.equals(u.getUsername()) 
                    && password.equals(u.getPassword()))
            .findFirst()
            .orElse(null);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public java.util.List<User> getAllOtherUsers(Long currentUserId) {
        return userRepository.findByIdNot(currentUserId);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public String generateOtp() {
        Random random = new Random();
        int otpNumber = 100000 + random.nextInt(900000);
        return String.valueOf(otpNumber);
    }

    public void registerUser(String username, String email) {
        Optional<User> existingUser = userRepository.findByEmail(email);

        User user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
            user.setUsername(username);
        } else {
            user = new User();
            user.setUsername(username);
            user.setEmail(email);
        }

        String otp = generateOtp();
        user.setOtp(otp);
        user.setVerified(false);

        userRepository.save(user);
        emailService.sendOtpEmail(email, otp);
    }

    public boolean verifyOtp(String email, String enteredOtp) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (user.getOtp() != null && user.getOtp().equals(enteredOtp)) {
                user.setVerified(true);
                user.setOtp(null);
                userRepository.save(user);
                return true;
            }
        }

        return false;
    }
}