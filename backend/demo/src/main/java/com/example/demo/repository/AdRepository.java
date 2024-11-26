package com.example.demo.repository;

import com.example.demo.model.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {
    // Find ads by user ID
    List<Ad> findByUserId(Long userId);
    List<Ad> findByUserIdOrderByIsFeaturedDesc(Long userId);
    Optional<Ad> findById(Long ad_id);

}
