package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Product;
import com.example.demo.service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;


    @PostMapping("/addToCart")
    public String addToCart(@RequestParam Long user_id , @RequestParam Long product_id){
        return cartService.addToCart(user_id , product_id);
    }

    @GetMapping("/getProductsInCart")
    public List<Product> getProductsInCart(@RequestParam Long user_id){
        return cartService.getProductsInCart(user_id);
    }

    @DeleteMapping("/deleteProductFromCart")
    public String deleteProductFromCart(@RequestParam Long user_id , @RequestParam Long product_id){
        return cartService.deleteProductFromCart(user_id ,product_id);
    }
    
}
