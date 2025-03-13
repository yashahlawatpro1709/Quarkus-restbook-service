package org.agoncal.quarkus.starting;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class LibraryService {
    @Inject
    BookRepository bookRepository;

    public List<Book> getOverdueBooks() {
        return bookRepository.getAllBooks().stream().filter(Book::isOverdue).collect(Collectors.toList());
    }
    public void notifyOverdueUsers(){
        List<Book> overdueBooks=getOverdueBooks();
        for(Book book:overdueBooks){
            System.out.println("Reminder: " + book.borrowedBy + ", your book '" + book.title + "' is overdue!");
        }
    }
}
