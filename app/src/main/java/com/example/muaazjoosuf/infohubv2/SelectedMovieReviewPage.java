package com.example.muaazjoosuf.infohubv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

public class SelectedMovieReviewPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_movie_review_page);


        Intent intent = getIntent();
        String section = intent.getStringExtra("section");
        String query = intent.getStringExtra("query");


        SelectedMovieCallHandler sm = new SelectedMovieCallHandler(query,section);
        sm.sortJSONDataIntoArrays();

        RecyclerView.Adapter adapter = new RecylerViewAdapterReviewPage(this,query,section);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_reviews);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);




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
