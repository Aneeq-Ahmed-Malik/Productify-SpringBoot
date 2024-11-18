package com.example.demo.model;

import jakarta.persistence.*;
import java.util.*;


@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000) // Allows longer descriptions (up to 1000 characters)
    private String title;

    @Column(length = 5000) // Allows longer descriptions (up to 1000 characters)
    private String description;
    
    @Column(length = 3000)
    private String link;
    private Double rating;
    private Double price;
    private String image1;
    private String image2;
    private String image3;
    private String image4;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "website_id")
    private Website website;

    public Product(String title, String description, Double price, String link, Double rating, String image1, String image2, String image3, String image4) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.link = link;
        this.rating = rating;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
    }

    public void setCategory(Category cat){
        this.category = cat;
    }

    public void setWebsite(Website web){
        this.website = web;
    }
    

}
