package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
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

    public List<Product> getProductsInCart(Long user_id) {
        Optional<Cart> userCartOptional = cartRepository.findByUserId(user_id);
        if (userCartOptional.isPresent()) {
            return userCartOptional.get().getProducts();
        } 
        return null;
    }

    public String deleteProductFromCart(Long user_id, Long product_id) {
        // Find the user's cart
        Optional<Cart> userCartOptional = cartRepository.findByUserId(user_id);
    
        if (userCartOptional.isPresent()) {
            Cart cart = userCartOptional.get();
    
            // Find and remove the product from the cart
            Product productToRemove = cart.getProducts().stream()
                .filter(product -> product.getId().equals(product_id))
                .findFirst()
                .orElse(null);
    
            if (productToRemove != null) {
                // Remove the product from the cart's product list
                cart.getProducts().remove(productToRemove);
                cartRepository.save(cart); // Save the updated cart
    
                // Remove the cart reference from the product if bidirectional
                if (productToRemove.getCarts() != null) {
                    productToRemove.getCarts().remove(cart);
                }
    
                productRepository.save(productToRemove);
    
                return "Product removed from cart and deleted successfully.";
            }
            return "Product not found in cart.";
        }
    
        return "Cart not found for the user.";
    }
    
    

    
}
