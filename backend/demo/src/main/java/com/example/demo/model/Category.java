package com.example.demo.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "category")
public class Category {

    @Id
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category")
    private List<Product> products;

    // Getters and Setters
    public void setName(String name){
        this.name = name;
    }

    public void setId(long id){
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
