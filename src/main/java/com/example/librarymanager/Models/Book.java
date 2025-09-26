package com.example.librarymanager.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public String title;
    public String author;
    public String isbn;
    public boolean isBorrowed = false;
    public LocalDate borrowDate;
    public LocalDate dueDate;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "borrowed_by_user_id")
    private User borrowedBy;

    public static final double FINE_PER_DAY = 1.0;

    public Book() {}
    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }
}
