package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Cart;
import com.example.demo.model.User;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Method to register a new user
    public String registerUser(String name, String email, String password) {
        // Check if email already exists
        User existingUser = userRepository.findByEmail(email);
        if (existingUser != null) {
            return "Email is already taken!";
        }
    
        // Create new user
        User user = new User(name, email, password);
    
        // Create a new cart and associate it with the user
        Cart cart = new Cart(user); // Setting the user in the cart
    
        // Save the cart first, so the cart is persisted before setting it in the user
    
        // Now set the cart in the user object
        user.setCart(cart);
    
        // Save the user
        userRepository.save(user);
    
        return "User registered successfully!";
    }
    

    public User loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if(user!= null && password.equals(user.getPassword()))
            return user;
        return null;
    }
}
