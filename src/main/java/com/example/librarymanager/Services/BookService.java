package com.example.librarymanager.Services;

import com.example.librarymanager.Models.Book;
import com.example.librarymanager.Models.User;

import java.util.List;

public interface BookService {

    Book addBook(Book book, User user);

    List<Book> getAvailableBooks(User user);

    List<Book> getBorrowedBooks(User user);

    Book borrowBook(int bookId, User user);

    Book returnBook(int bookId, User user);

    void removeBook(int bookId, User user);
}
