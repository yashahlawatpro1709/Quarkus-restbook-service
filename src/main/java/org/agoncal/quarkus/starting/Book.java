package org.agoncal.quarkus.starting;

import io.smallrye.common.constraint.NotNull;
import java.time.temporal.ChronoUnit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Book {
    public int id;
    @NotNull
    public String title;
    public String author;
    public int yearOfPublication;
    public String genre;
    private boolean isBorrowed = false;
    public String borrowedBy = null;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private List<Review> reviews =new ArrayList<>();
    public Book() {
    }

    public Book(int id, String title, String author, int yearOfPublication, String genre) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.yearOfPublication = yearOfPublication;
        this.genre = genre;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return yearOfPublication;
    }

    public void setYear(int year) {
        this.yearOfPublication = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
    public boolean borrowBook(String username){
        if(!isBorrowed){
            isBorrowed=true;
            borrowedBy=username;
            borrowDate= LocalDate.now();
            dueDate = borrowDate.plusDays(14);
            return true;
        }
        return false;
    }
    public boolean returnBook(){
        if(isBorrowed){
            isBorrowed=false;
            borrowedBy=null;
            borrowDate=null;
            dueDate=null;
            return true;
        }
        return false;
    }
    public boolean isOverdue(){
        return isBorrowed && LocalDate.now().isEqual(dueDate);
    }
    public void addReview(Review review){
        reviews.add(review);
    }
    public List<Review> getReviews(){
        return reviews;
    }
}
