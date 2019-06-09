package com.example.muaazjoosuf.infohubv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

public class SelectedMovieCastsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_movie_casts_page);

        Intent intent = getIntent();
        String query = intent.getStringExtra("query");
        String section = intent.getStringExtra("section");

        RecyclerView.Adapter adapter = new RecylerViewAdapterCastsPage(this,query,"cast",section);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_casts);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        RecyclerView.Adapter adapter1 = new RecylerViewAdapterCastsPage(this,query,"crew",section);
        RecyclerView recyclerView1 = findViewById(R.id.recycler_view_crew);
        RecyclerView.LayoutManager layoutManager1 = new GridLayoutManager(this,3);
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView1.setAdapter(adapter1);

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
