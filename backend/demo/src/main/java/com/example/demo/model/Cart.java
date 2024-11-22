package com.example.demo.model;

import java.util.ArrayList;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToMany;


@Entity
@Table(name = "Cart")
public class Cart {

    @Id
    private Long id;

    @OneToOne
    private User user;

    @ManyToMany(mappedBy = "carts")
    private ArrayList<Product> products;

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
}

