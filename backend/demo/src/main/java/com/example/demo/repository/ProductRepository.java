package com.example.demo.repository;

import com.example.demo.model.*;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
    List<Product> findAll();

    List<Product> findByCategoryName(String categoryName);

    List<Product> findByWebsiteName(String websiteName);

    List<Product> findByTitleContainingIgnoreCase(String title);

    List<Product> findByPriceBetween(double minPrice, double maxPrice);
}
