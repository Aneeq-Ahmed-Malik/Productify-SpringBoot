package com.example.demo.repository;

import com.example.demo.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WebsiteRepository extends JpaRepository<Website, Long>  {
    public Website findByName(String name);

    @Query("SELECT w.id FROM Website w WHERE w.name = :websiteName")
    Long findWebIdByWebsiteName(@Param("websiteName") String websiteName);
}
