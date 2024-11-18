package com.example.demo.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "website")

public class Website {

    @Id
    private Long id;

    private String name;

    @OneToMany(mappedBy = "website")
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
