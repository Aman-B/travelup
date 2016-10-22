package com.codefundo.travelup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Work on 10/22/2016.
 */

public class ReviewAdapter extends ArrayAdapter<Review> {
    ImageView mpicture;


    public ReviewAdapter(Context context, ArrayList<Review> review) {
        super(context, 0, review);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Review review = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_review, parent, false);
        }
        // Lookup view for data population
        TextView title = (TextView) convertView.findViewById(R.id.reviewtitle);
        TextView body = (TextView) convertView.findViewById(R.id.reviewbody);
        TextView username = (TextView) convertView.findViewById(R.id.reviewusername);
        mpicture=(ImageView) convertView.findViewById(R.id.imageView2);
        // Populate the data into the template view using the data object
        title.setText(review.getReviewTitle());
        body.setText(review.getReviewBody());
        username.setText(review.getUserName());
        mpicture.setImageBitmap(review.getImage());
        // Return the completed view to render on screen
        return convertView;
}
}