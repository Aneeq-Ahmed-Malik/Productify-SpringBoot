package com.example.demo.repository;

import com.example.demo.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, Long>  {
    public Category findByName(String name);

    @Query("SELECT c.id FROM Category c WHERE c.name = :categoryName")
    Long findCatIdByCategoryName(@Param("categoryName") String categoryName);
}
