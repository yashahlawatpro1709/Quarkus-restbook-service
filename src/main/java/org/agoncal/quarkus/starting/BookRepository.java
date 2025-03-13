package org.agoncal.quarkus.starting;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class BookRepository {
    private final List<Book> books = new ArrayList<>(List.of(
            new Book(1, "The Hitchhiker's Guide", "Douglas Adams", 1979, "Science fiction"),
            new Book(2, "Data2", "Author2", 1977, "Something"),
            new Book(3, "Data3", "Author3", 1974, "New123"),
            new Book(4, "Data4", "Author4", 1980, "New genre"),
            new Book(5, "Data5", "Author5", 1986, "New genre")
    ));

    public List<Book> getAllBooks() {
        return books;
    }

    public int countAllBooks() {
        return books.size();
    }

    public Optional<Book> getBook(int id) {
        return books.stream().filter(book -> book.id == id).findFirst();
    }

    public void addBook(Book book) {
        books.add(book);  // Now it will work as 'books' is mutable
    }

    public boolean borrowBook(int id, String username) {
        Optional<Book> book = getBook(id);
        return book.map(b -> b.borrowBook(username)).orElse(false);
    }

    public boolean returnBook(int id) {
        Optional<Book> book = getBook(id);
        return book.map(Book::returnBook).orElse(false);
    }

    public boolean addReview(int bookId, Review review) {
        Optional<Book> book = getBook(bookId);
        if (book.isPresent()) {
            book.get().addReview(review);
            return true;
        }
        return false;

    }
    public List<Review> getReviews(int bookId){
        return getBook(bookId).map(Book::getReviews).orElse(List.of());
    }
}

