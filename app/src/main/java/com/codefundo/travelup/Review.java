package com.codefundo.travelup;

import android.graphics.Bitmap;

/**
 * Created by Work on 10/22/2016.
 */

public class Review{
    private String username;
    private String userid;
    private String reviewTitle;
    private String reviewBody;
    private Bitmap mBitmap;

    //Getters
    public Review(String username, String reviewTitle, String reviewBody,Bitmap mBitmap) {
        this.username = username;
        this.reviewTitle = reviewTitle;
        this.reviewBody = reviewBody;
        this.mBitmap=mBitmap;
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

    public  Bitmap getImage(){return  mBitmap;}

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

    public void setmBitmap(Bitmap mBitmap){
        this.mBitmap=mBitmap;
    }


}
