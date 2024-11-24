package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Ad;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {
    // Find ads by user ID
    List<Ad> findByUserId(Long userId);
}
