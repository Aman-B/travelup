package com.codefundo.travelup;

/**
 * Created by Work on 10/22/2016.
 */

public class Review{
    private String username;
    private String userid;
    private String reviewTitle;
    private String reviewBody;

    //Getters
    public Review(String username, String reviewTitle, String reviewBody) {
        this.username = username;
        this.reviewTitle = reviewTitle;
        this.reviewBody = reviewBody;
    }

    public String getUserName(){
        return username;
    }

    public String getUserId(){
        return userid;
    }

    public String getReviewTitle(){
        return reviewTitle;
    }

    public String getReviewBody(){
        return reviewBody;
    }

    //Setters

    public void setUserName(String username){
        this.username = username;
    }

    public void setUserId(String username){
        this.userid = userid;
    }

    public void setReviewBody(String reviewBody){
        this.reviewBody = reviewBody;
    }

    public void setReviewTitle(String reviewTitle){
        this.reviewTitle = reviewTitle;
    }

}
