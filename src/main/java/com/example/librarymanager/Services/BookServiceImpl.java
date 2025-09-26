package com.example.librarymanager.Services;

import com.example.librarymanager.Models.Book;
import com.example.librarymanager.Models.User;
import com.example.librarymanager.AppDataContext.BookRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private static final int MAX_BORROW = 3;
    private static final int BORROW_DAYS = 14;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book addBook(Book book, User user) {
        if (user == null || !user.isAdmin()) throw new RuntimeException("Only admins can add books!");
        return bookRepository.save(book);
    }

    @Override
    public List<Book> getAvailableBooks(User user) {
        if (user == null) throw new RuntimeException("You must be logged in!");
        return bookRepository.findAll().stream().filter(b -> !b.isBorrowed()).toList();
    }

    @Override
    public List<Book> getBorrowedBooks(User user) {
        if (user == null) throw new RuntimeException("You must be logged in!");
        return bookRepository.findAll().stream()
                .filter(b -> b.isBorrowed() && b.getBorrowedBy().getId() == user.getId())
                .toList();
    }

    @Override
    public Book borrowBook(int bookId, User user) {
        if (user == null) throw new RuntimeException("You must be logged in!");
        long borrowedCount = bookRepository.findAll().stream()
                .filter(b -> b.isBorrowed() && b.getBorrowedBy().getId() == user.getId())
                .count();
        if (borrowedCount >= MAX_BORROW) throw new RuntimeException("Maximum borrow limit reached");

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found!"));
        if (book.isBorrowed()) throw new RuntimeException("Book is already borrowed!");

        book.setBorrowed(true);
        book.setBorrowedBy(user);
        book.setBorrowDate(LocalDate.now());
        book.setDueDate(LocalDate.now().plusDays(BORROW_DAYS));

        return bookRepository.save(book);
    }

    @Override
    public Book returnBook(int bookId, User user) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found!"));
        if (!book.isBorrowed() || book.getBorrowedBy().getId() != user.getId())
            throw new RuntimeException("You did not borrow this book!");

        if (book.getDueDate().isBefore(LocalDate.now())) {
            long overdueDays = ChronoUnit.DAYS.between(book.getDueDate(), LocalDate.now());
            double fine = overdueDays * Book.FINE_PER_DAY;
            System.out.println("Book overdue by " + overdueDays + " day(s). Fine: " + fine);
        }

        book.setBorrowed(false);
        book.setBorrowedBy(user);
        book.setBorrowDate(null);
        book.setDueDate(null);

        return bookRepository.save(book);
    }

    @Override
    public void removeBook(int bookId, User user) {
        if (user == null || !user.isAdmin()) throw new RuntimeException("Only admins can remove books!");
        if (!bookRepository.existsById(bookId)) throw new RuntimeException("Book not found!");
        bookRepository.deleteById(bookId);
    }
}
