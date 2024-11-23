package com.example.demo.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Cart;
import com.example.demo.model.Product;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    // Add product to cart
    public String addToCart(Long userId, Long productId) {
        // Fetch the cart by user ID
        Optional<Cart> optionalCart = cartRepository.findByUserId(userId);
    
        // Fetch the product by ID
        Optional<Product> optionalProduct = productRepository.findById(productId);
    
        if (optionalCart.isPresent() && optionalProduct.isPresent()) {
            Cart cart = optionalCart.get();
            Product product = optionalProduct.get();
    
            // Initialize the cart's product list if null
            if (cart.getProducts() == null) {
                cart.setProducts(new ArrayList<Product>());
            }
    
            // Add the product to the cart's products list if not already present
            if (!cart.getProducts().contains(product)) {
                cart.getProducts().add(product);
    
                // Maintain the bidirectional relationship
                if (product.getCarts() == null) {
                    product.setCarts(new ArrayList<>());
                }
                if (!product.getCarts().contains(cart)) {
                    product.getCarts().add(cart);
                }
    
                // Save the updated cart
                cartRepository.save(cart);
    
                // Optionally save the updated product
                productRepository.save(product); // This is optional, as Hibernate may handle it
            }
        } else {
           return "Product not added to Cart";
        }
        return "Product added successfully";
    }
    
}
