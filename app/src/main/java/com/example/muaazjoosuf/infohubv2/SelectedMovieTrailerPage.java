package com.example.muaazjoosuf.infohubv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SelectedMovieTrailerPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_movie_trailer_page);

        Intent intent = getIntent();
        String section = intent.getStringExtra("section");
        String query = intent.getStringExtra("query");


        SelectedMovieCallHandler sm = new SelectedMovieCallHandler(query,section);
        sm.sortJSONDataIntoArrays();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
    }


    //Button to make sure that when back button is pressed it exists intent
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
