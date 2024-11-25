package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "Ad")

public class Ad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many-to-one relationship with User
    @ManyToOne
    @JoinColumn(name = "user_id")  // Foreign key column name
     
    private User user;  // Reference to User

    // Fields for the ad
    private String title;  // Title of the ad
    private String description;  // Description of the ad
    private String phoneNo;  // Contact phone number
    private Long price;  // Price of the item
    private String location;  // Location related to the ad

    // Image URLs or paths
    private String image1;  
    private String image2;  
    private String image3;  
    private String image4;  
    boolean isFeatured;

    // Parameterized constructor
    public Ad(){
        isFeatured=false;
    }


    public Ad(User user, String title, String description, String phoneNo, Long price, String location) {
        this.user = user;
        this.title = title;
        this.description = description;
        this.phoneNo = phoneNo;
        this.price = price;
        this.location = location;
        isFeatured=false;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public boolean getisFeatured() {
        return isFeatured;
    }

    public void setisFeatured(boolean  isFeatured) {
        this.isFeatured = isFeatured;
    }
}
