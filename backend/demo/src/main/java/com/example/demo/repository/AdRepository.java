package com.example.demo.repository;

import com.example.demo.model.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {
    // Find ads by user ID
    List<Ad> findByUserId(Long userId);
    List<Ad> findByUserIdOrderByIsFeaturedDesc(Long userId);

}
