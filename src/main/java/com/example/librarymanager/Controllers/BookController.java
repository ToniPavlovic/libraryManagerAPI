package com.example.librarymanager.Controllers;

import com.example.librarymanager.Middleware.UserNotLoggedInException;
import com.example.librarymanager.Models.Book;
import com.example.librarymanager.Models.User;
import com.example.librarymanager.Services.BookService;
import com.example.librarymanager.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
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

    private User getCurrentUser(Authentication auth) {
        if (auth == null || auth.getName() == null) {
            throw new UserNotLoggedInException();
        }
        return userService.findByUsername(auth.getName())
                .orElseThrow(UserNotLoggedInException::new);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<Book> getAllBooks(Authentication auth) {
        User user = getCurrentUser(auth);
        return bookService.getAllBooks(user);
    }

    @GetMapping("/available")
    @ResponseStatus(HttpStatus.OK)
    public List<Book> getAvailableBooks(Authentication auth, @RequestParam(required = false) Integer userId) {
        User user = getCurrentUser(auth);
        return bookService.getAvailableBooks(user);
    }

    @GetMapping("/borrowed")
    @ResponseStatus(HttpStatus.OK)
    public List<Book> getBorrowedBooks(Authentication auth, @RequestParam(required = false) Integer userId) {
        User user = getCurrentUser(auth);
        return bookService.getBorrowedBooks(user);
    }

    @PostMapping("/borrow/{bookId}")
    @ResponseStatus(HttpStatus.OK)
    public Book borrowBook(@PathVariable Integer bookId, Authentication auth, @RequestParam(required = false) Integer userId) {
        User user = getCurrentUser(auth);
        return bookService.borrowBook(bookId, user);
    }

    @PostMapping("/return/{bookId}")
    @ResponseStatus(HttpStatus.OK)
    public Book returnBook(@PathVariable int bookId, Authentication auth, @RequestParam(required = false) Integer userId) {
        User user = getCurrentUser(auth);
        return bookService.returnBook(bookId, user);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@Valid @RequestBody Book book, Authentication auth, @RequestParam(required = false) Integer userId) {
        User user = getCurrentUser(auth);
        return bookService.addBook(book, user);
    }

    @DeleteMapping("/{bookId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int bookId, Authentication auth, @RequestParam(required = false) Integer userId) {
        User user = getCurrentUser(auth);
        bookService.removeBook(bookId, user);
    }

    @GetMapping("/author/{author}")
    @ResponseStatus(HttpStatus.OK)
    public List<Book> getBooksByAuthor(@PathVariable String author, Authentication auth, @RequestParam(required = false) Integer userId) {
        User user = getCurrentUser(auth);
        return bookService.getBooksByAuthor(author, user);
    }
}
