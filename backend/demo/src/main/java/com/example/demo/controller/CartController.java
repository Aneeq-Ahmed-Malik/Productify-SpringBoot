package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Product;
import com.example.demo.service.CartService;
import com.example.demo.service.NotificationManager;
import com.example.demo.service.RecommendationService;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationManager notificationService;

    @Autowired
    private RecommendationService recommendationService;

    @CrossOrigin(origins = {"https://productify.live", "https://www.productify.live"})
    @PostMapping("/addToCart")
    public String addToCart(@RequestParam Long user_id , @RequestParam Long product_id){
        String message = cartService.addToCart(user_id , product_id);

        List<Product> products = cartService.getProductsInCart(user_id);
        List<Long> ids = new ArrayList<Long>();

        for (Product p : products){
            ids.add(p.getId());
        }

        List<Product> recommendations = recommendationService.getRecommendations(ids, 2);
        notificationService.notifyUserOfRecommendedProducts(userService.getUserById(user_id).get(), recommendations);
        return message;
    }

    @CrossOrigin(origins = {"https://productify.live", "https://www.productify.live"})
    @GetMapping("/getProductsInCart")
    public List<Product> getProductsInCart(@RequestParam("user_id") Long userId) {
        return cartService.getProductsInCart(userId);
    }
    
    @CrossOrigin(origins = {"https://productify.live", "https://www.productify.live"})
    @DeleteMapping("/deleteProductFromCart")
    public String deleteProductFromCart(@RequestParam Long user_id , @RequestParam Long product_id){
        return cartService.deleteProductFromCart(user_id ,product_id);
    }
    
}
