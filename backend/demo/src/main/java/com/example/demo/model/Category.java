package com.example.demo.model;

import jakarta.persistence.*;
import java.util.List;

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
