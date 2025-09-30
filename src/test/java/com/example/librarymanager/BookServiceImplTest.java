package com.example.librarymanager;

import com.example.librarymanager.Middleware.*;
import com.example.librarymanager.Models.*;
import com.example.librarymanager.AppDataContext.BookRepository;
import com.example.librarymanager.Services.BookServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    private final BookRepository bookRepo = mock(BookRepository.class);
    private final BookServiceImpl bookService = new BookServiceImpl(bookRepo);

    @Test
    void addBook_ShouldThrowUserNotAdminException_WhenUserIsNotAdmin() {
        User normalUser = new User(6, "Bob", "pass", false);

        assertThrows(UserNotAdminException.class,
                () -> bookService.addBook(new Book(), normalUser));
    }

    @Test
    void getAvailableBooks_ShouldThrowUserNotLoggedInException_WhenUserIsNull() {
        assertThrows(UserNotLoggedInException.class,
                () -> bookService.getAvailableBooks(null));
    }

    @Test
    void borrowBook_ShouldThrowBookNotFoundException_WhenBookDoesNotExist() {
        User admin = new User(99, "Alice", "pass", true);
        when(bookRepo.findById(99)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class,
                () -> bookService.borrowBook(99, admin));
    }
}
