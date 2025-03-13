package org.agoncal.quarkus.starting;

public class Review {
    private String username;
    private int rating;
    private String reviewText;
    public Review(){
    }
    public Review(String username,int rating,String reviewText){
        this.username=username;
        this.rating=rating;
        this.reviewText=reviewText;
    }
    public String getUsername(){
        return username;
    }
    public int getRating(){
        return rating;
    }
    public String getReviewText(){
        return reviewText;
    }
}
