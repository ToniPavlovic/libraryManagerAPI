package com.example.librarymanager.Controllers;

import com.example.librarymanager.Models.Book;
import com.example.librarymanager.Models.User;
import com.example.librarymanager.Services.BookService;
import com.example.librarymanager.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final UserService userService;

    public BookController(BookService bookService, UserService userService) {
        this.bookService = bookService;
        this.userService = userService;
    }

    @GetMapping("")
    public List<Book> getAvailableBooks(@RequestParam Integer userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        return bookService.getAvailableBooks(user);
    }

    @PostMapping("/borrow/{bookId}")
    public Book borrowBook(@PathVariable Integer bookId, @RequestParam Integer userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        return bookService.borrowBook(bookId, user);
    }

    @PostMapping("/return/{bookId}")
    public Book returnBook(@PathVariable int bookId, @RequestParam Integer userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        return bookService.returnBook(bookId, user);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@Valid @RequestBody Book book, @RequestParam Integer userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        return bookService.addBook(book, user);
    }

    @DeleteMapping("/{bookId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable int bookId, @RequestParam Integer userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        bookService.removeBook(bookId, user);
    }

    @GetMapping("/author/{author}")
    public List<Book> findByAuthor(@PathVariable String author, @RequestParam Integer userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        return bookService.getAvailableBooks(user).stream()
                .filter(b -> b.getAuthor().equalsIgnoreCase(author))
                .toList();
    }
}
