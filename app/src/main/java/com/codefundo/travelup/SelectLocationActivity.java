package com.codefundo.travelup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

public class SelectLocationActivity extends AppCompatActivity {

    private Integer THRESHOLD = 2;
    private DelayAutoCompleteTextView geo_autocomplete;
    private ImageView geo_autocomplete_clear;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);

        geo_autocomplete_clear = (ImageView) findViewById(R.id.geo_autocomplete_clear);

        geo_autocomplete = (DelayAutoCompleteTextView) findViewById(R.id.geo_autocomplete);
        geo_autocomplete.setThreshold(THRESHOLD);
        geo_autocomplete.setAdapter(new GeoAutoCompleteAdapter(this)); // 'this' is Activity instance

        geo_autocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                GeoSearchResult result = (GeoSearchResult) adapterView.getItemAtPosition(position);
                geo_autocomplete.setText(result.getAddress());
            }
        });

        geo_autocomplete.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0)
                {
                    geo_autocomplete_clear.setVisibility(View.VISIBLE);
                }
                else
                {
                    geo_autocomplete_clear.setVisibility(View.GONE);
                }
            }
        });

        geo_autocomplete_clear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                geo_autocomplete.setText("");
            }
        });

    }
}
