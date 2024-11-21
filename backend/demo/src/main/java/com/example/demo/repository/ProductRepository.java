package com.example.demo.repository;

import com.example.demo.model.*;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
    List<Product> findAll();

     @Query(value = """
        SELECT *
        FROM (
            SELECT p.*, ROW_NUMBER() OVER (PARTITION BY p.website_id ORDER BY p.id) AS row_num
            FROM product p
            WHERE p.category_id = :catId
        ) AS subquery
        ORDER BY row_num, website_id
        """, nativeQuery = true)
    List<Product> findByCategoryId(Long catId);

    List<Product> findByWebsiteName(String websiteName);

    List<Product> findByTitleContainingIgnoreCase(String title);

    List<Product> findByPriceBetween(double minPrice, double maxPrice);

    List<Product> findByCategoryNameAndWebsiteName(String categoryName, String websiteName);
}
