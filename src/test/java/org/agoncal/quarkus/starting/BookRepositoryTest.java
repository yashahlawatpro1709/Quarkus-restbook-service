package org.agoncal.quarkus.starting;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class BookRepositoryTest {

    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        bookRepository = new BookRepository();
    }

    @Test
    void testGetAllBooks() {
        List<Book> books = bookRepository.getAllBooks();
        assertNotNull(books);
        assertFalse(books.isEmpty());
    }

    @Test
    void testGetBookById() {
        Optional<Book> book = bookRepository.getBook(1);
        assertTrue(book.isPresent());
        assertEquals(1, book.get().id);
    }

    @Test
    void testGetBookById_NotFound() {
        Optional<Book> book = bookRepository.getBook(99);
        assertFalse(book.isPresent());
    }

    @Test
    void testCountAllBooks() {
        int count = bookRepository.countAllBooks();
        assertEquals(5, count);
    }

    @Test
    void testAddBook() {
        Book newBook = new Book(6, "New Book", "New Author", 2024, "Fantasy");
        bookRepository.addBook(newBook);
        assertEquals(6, bookRepository.countAllBooks());
    }

    @Test
    void testBorrowBook_Success() {
        boolean borrowed = bookRepository.borrowBook(1, "JohnDoe");
        assertTrue(borrowed);
    }

    @Test
    void testBorrowBook_Fail_InvalidId() {
        boolean borrowed = bookRepository.borrowBook(99, "JohnDoe");
        assertFalse(borrowed);
    }

    @Test
    void testReturnBook_Success() {
        bookRepository.borrowBook(1, "JohnDoe");
        boolean returned = bookRepository.returnBook(1);
        assertTrue(returned);
    }

    @Test
    void testReturnBook_Fail_NotBorrowed() {
        boolean returned = bookRepository.returnBook(2);
        assertFalse(returned);
    }

    @Test
    void testAddReview_Success() {
        Review review = new Review("Alice", 5, "Great book!");
        boolean added = bookRepository.addReview(1, review);
        assertTrue(added);
    }

    @Test
    void testAddReview_Fail_InvalidBookId() {
        Review review = new Review("Alice", 2, "Good");
        boolean added = bookRepository.addReview(99, review); // Non-existent book
        assertFalse(added);
    }

    @Test
    void testGetReviews_Success() {
        Review review = new Review("Bob", 4, "Nice book");
        bookRepository.addReview(1, review);
        List<Review> reviews = bookRepository.getReviews(1);
        assertFalse(reviews.isEmpty());
        assertEquals(1, reviews.size());
    }

    @Test
    void testGetReviews_Empty() {
        List<Review> reviews = bookRepository.getReviews(99);
        assertTrue(reviews.isEmpty());
    }
}

