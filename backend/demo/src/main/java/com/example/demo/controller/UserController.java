package com.example.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.service.NotificationManager;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private NotificationManager notificationService;

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> userData) {
        try {
            String email = userData.get("email");
            String password = userData.get("password");
            String name = userData.get("name");

            User user = userService.registerUser(name, email, password);
            if (user != null){
                notificationService.notifyUserOfSignUp(user);
                return ResponseEntity.ok(Map.of("message", "Success"));
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Already Registerted."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/login")
    public User loginUser(@RequestParam("email") String email , @RequestParam("password") String password) {
        // String email = userData.get("email");
        // String password = userData.get("password");
        return userService.loginUser(email, password);
        
    }

}
