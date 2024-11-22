package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Method to register a new user
    public String registerUser(String name, String email, String password) {
        // Check if email already exists
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            return "Email is already taken!";
        }        
        // Create new user and save
        User user = new User(name,email,password);

        userRepository.save(user);
        return "User registered successfully!";
    }

    public String loginUser(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Check if the provided password matches the stored password (hashed)
            if (password == user.getPassword()) {
                return "Login successful!";
            } else {
                return "Invalid password.";
            }
        } else {
            return "User not found with the provided email.";
        }
    }
}
