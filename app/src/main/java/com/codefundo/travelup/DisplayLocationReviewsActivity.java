package com.codefundo.travelup;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

public class DisplayLocationReviewsActivity extends AppCompatActivity {

    ArrayList<String> friends;
    String location;
    ListView lv_reviewlist;
    ReviewAdapter adapter;

    private void getParameters(){
        Bundle extras = getIntent().getExtras();
        friends = new ArrayList<String>();
        if(extras != null) {
            friends = extras.getParcelable("USER-FILTERED-FRIEND-LIST");
            location = extras.getString("USER-SELECTED-LOCATION");
        }
        else{
            Log.v("--", "Activity DisplayLocationReviewsActivity: Error retrieving user's friends from extras");
        }
    }

    private ArrayList<Review> queryReviews(){
        SQLiteDatabase db = openOrCreateDatabase("travelup",MODE_PRIVATE,null);
        ArrayList<Review> reviews = new ArrayList<>();
        for(String friend : friends){
            //Query DB for friend's reviews
            Cursor cursor = db.query("REVIEWS", new String[] {"username", "review_title", "review_body"}, "where " +
                    "userid = ? and location = ?", new String[] {friend, location}, null, null, null, null);
            //If resultset is not empty
            if(cursor.moveToFirst()){
                //For all result tuples
                do{
                    //Parse tuple into Review Object
                    String review_username = cursor.getString(cursor.getColumnIndex("username"));
                    String review_title = cursor.getString(cursor.getColumnIndex("review_title"));
                    String review_body = cursor.getString(cursor.getColumnIndex("review_body"));
                    Review review = new Review(review_username, review_title, review_body);
                    //Add review object to ArrayList
                    reviews.add(review);

                } while(cursor.moveToNext());
            }
        }
        return reviews;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_location_reviews);
        getParameters();
        ArrayList<Review> reviews = queryReviews();
        adapter = new ReviewAdapter(this, reviews);
        lv_reviewlist = (ListView) findViewById(R.id.reviewlist);
        lv_reviewlist.setAdapter(adapter);
    }

}
