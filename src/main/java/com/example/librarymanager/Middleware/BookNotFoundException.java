package com.example.librarymanager.Middleware;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(){
        super("Book not found!");
    }
}
