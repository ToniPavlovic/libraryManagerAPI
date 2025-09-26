package com.example.librarymanager.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String password;

    @Column(name = "isAdmin", nullable = false)
    @JsonProperty("admin")
    private boolean admin;

    @OneToMany(mappedBy = "borrowedBy", cascade = CascadeType.ALL)
    private List<Book> borrowedBooks;

    public User(){}

    public User (int id, String name, String password, boolean admin){
        this.id = id;
        this.name = name;
        this.password = password;
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + name;
    }
}
