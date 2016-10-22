package com.codefundo.travelup;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class DisplayLocationReviewsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ArrayList<String> friends;
    String location;
    ListView lv_reviewlist;
    ReviewAdapter adapter;
    Bitmap bitmap;

    private void getParameters(){
        Bundle extras = getIntent().getExtras();
        friends = new ArrayList<String>();
        if(extras != null) {
            friends = extras.getStringArrayList("USER-FILTERED-FRIEND-LIST");
            location = extras.getString("USER-SELECTED-LOCATION");
            byte[] byteArray = getIntent().getByteArrayExtra("bitmap");
            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            Toast.makeText(getApplicationContext(), "Here", Toast.LENGTH_SHORT).show();
        }
        else{
            friends.add("Aman");
            location="Manipal";
            bitmap= BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

            Log.v("--", "Activity DisplayLocationReviewsActivity: Error retrieving user's friends from extras");
        }
    }

    private ArrayList<Review> queryReviews(){
        ArrayList<Review> reviews = new ArrayList<>();
        int test=1;
        SQLiteDatabase db = openOrCreateDatabase("travelup",MODE_PRIVATE,null);
        if(test!=1) {
            for (String friend : friends) {
                //Query DB for friend's reviews
                Cursor cursor = db.query("REVIEWS", new String[]{"username", "review_title", "review_body"},
                        "userid = ? and location = ?", new String[]{friend, location}, null, null, null, null);
                //If resultset is not empty
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        //For all result tuples
                        do {
                            //Parse tuple into Review Object
                            String review_username = cursor.getString(cursor.getColumnIndex("username"));
                            String review_title = cursor.getString(cursor.getColumnIndex("review_title"));
                            String review_body = cursor.getString(cursor.getColumnIndex("review_body"));
                            Review review = new Review(review_username, review_title, review_body,bitmap);
                            //Add review object to ArrayList
                            reviews.add(review);

                        } while (cursor.moveToNext());
                    }
                }

            }
        }
            else
            {
                for(int i=0;i<6;i++) {
                    Review mReview = new Review("Aman", " Guide of Manipal ", " A great place to visit. Better to visit in January-March. The beaches are awesome. And so is the weather. "
                            + "Local cuisine isn't to be missed!",bitmap);
                    reviews.add(mReview);
                    Review mReview2 = new Review("Yash", " Guide of Manipal ", " A great place to visit. Better to visit in January-March. The beaches are awesome. And so is the weather. "
                            + "Local cuisine isn't to be missed!",bitmap);
                    reviews.add(mReview2);
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
        lv_reviewlist.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(DisplayLocationReviewsActivity.this, PlaceDetails.class);
        startActivity(intent);
    }
}
