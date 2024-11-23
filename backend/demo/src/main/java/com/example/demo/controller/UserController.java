package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.UserService;

import com.example.demo.model.User;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/signup")
    public String signup(@RequestBody User user) {
        return userService.registerUser(user.getName(), user.getEmail() , user.getPassword());
    }
    
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/login")
    public User loginUser(@RequestBody User user ) {
        return userService.loginUser(user.getEmail() , user.getPassword());
    }
}
