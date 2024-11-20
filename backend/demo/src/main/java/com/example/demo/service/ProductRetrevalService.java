package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductRetrevalService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Retrieve all products from the database.
     *
     * @return List of all products.
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Retrieve a product by its ID.
     *
     * @param id The ID of the product.
     * @return Optional containing the product if found, or empty otherwise.
     */
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    /**
     * Retrieve products by category name.
     *
     * @param categoryName The name of the category.
     * @return List of products belonging to the specified category.
     */
    public List<Product> getProductsByCategory(String categoryName) {
        return productRepository.findByCategoryName(categoryName);
    }

    /**
     * Retrieve products by website name.
     *
     * @param websiteName The name of the website.
     * @return List of products belonging to the specified website.
     */
    public List<Product> getProductsByWebsite(String websiteName) {
        return productRepository.findByWebsiteName(websiteName);
    }

    /**
     * Search for products by title (partial match).
     *
     * @param title The partial title to search for.
     * @return List of products matching the title.
     */
    public List<Product> searchProductsByTitle(String title) {
        return productRepository.findByTitleContainingIgnoreCase(title);
    }

    /**
     * Search for products within a price range.
     *
     * @param minPrice The minimum price.
     * @param maxPrice The maximum price.
     * @return List of products within the price range.
     */
    public List<Product> getProductsByPriceRange(double minPrice, double maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }
}
