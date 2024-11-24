package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Cart;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Method to register a new user
    public User registerUser(String name, String email, String password) {
        // Check if email already exists
        User existingUser = userRepository.findByEmail(email);
        if (existingUser != null) {
            return null;
        }
    
        User user = new User(name, email, password);
    
        Cart cart = new Cart(user); // Setting the user in the cart
    
        user.setCart(cart);
    
        userRepository.save(user);
    
        return user;
    }
    

    public User loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if(user!= null && password.equals(user.getPassword())){
            return user;
            
        }
        return null;
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
}
