package com.example.muaazjoosuf.infohubv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.takusemba.multisnaprecyclerview.MultiSnapRecyclerView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MoviesSectionActivity extends AppCompatActivity {


    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    FrameLayout progressBarHolder;
    TextView loadingText;

    MultiSnapRecyclerView.Adapter adapter;
    MultiSnapRecyclerView.Adapter adapter1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_section);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);


        progressBarHolder = findViewById(R.id.progressBarHolder);
        loadingText = findViewById(R.id.loading_text);

        new MyTask().execute();


    }

    private void floatingSearchViewSetup(){
        final FloatingSearchView mSearchView = (FloatingSearchView) findViewById(R.id.floating_search_view);
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                if(oldQuery.equals("") ||newQuery.equals("")){
                    mSearchView.hideProgress();
                    showItems();
                }else {
                    mSearchView.showProgress(); // displays a circular progress bar to show search progress
                }
            }
        });
        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

            }

            @Override
            public void onSearchAction(String currentQuery) {
                hideItems();
                RecyclerView.Adapter adapter = new SearchMovieAdapter(getApplicationContext(),currentQuery.trim(),"movie");
                RecyclerView recyclerView =(RecyclerView) findViewById(R.id.search_results_list);
                RecyclerView.LayoutManager layoutManager =
                        new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        String title = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.title_text_card_layout)).getText().toString();
                        Intent intent = new Intent(getApplicationContext(),SelectedMovieActivity.class);
                        intent.putExtra("title",title);


                        String tag = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.title_text_card_layout)).getTag().toString();
                        if(tag.equals("1")){
                            //series
                            intent.putExtra("section","series");
                        }
                        if(tag.equals("0")){
                            //movie
                            intent.putExtra("section","movie");
                        }
                        startActivity(intent);
                    }
                });
                mSearchView.hideProgress();
            }
        });
    }

    private void hideItems(){
        TextView movies,series;
        RecyclerView recyclerView;
        MultiSnapRecyclerView recyclerView1,recyclerView2;
        movies = findViewById(R.id.latestMoviesTag);
        series = findViewById(R.id.latestSeriesTag);
        recyclerView1 = findViewById(R.id.first_recycler_view);
        recyclerView2 = findViewById(R.id.second_recycler_view);
        recyclerView = findViewById(R.id.search_results_list);

        movies.setVisibility(View.GONE);
        series.setVisibility(View.GONE);
        recyclerView1.setVisibility(View.GONE);
        recyclerView2.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);


    }

    private void showItems(){
        TextView movies,series;
        RecyclerView recyclerView;
        MultiSnapRecyclerView recyclerView1,recyclerView2;
        movies = findViewById(R.id.latestMoviesTag);
        series = findViewById(R.id.latestSeriesTag);
        recyclerView1 = findViewById(R.id.first_recycler_view);
        recyclerView2 = findViewById(R.id.second_recycler_view);
        recyclerView = findViewById(R.id.search_results_list);

        movies.setVisibility(View.VISIBLE);
        series.setVisibility(View.VISIBLE);
        recyclerView1.setVisibility(View.VISIBLE);
        recyclerView2.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);


    }

    //Button to make sure that when back button is pressed it exists intent
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }




    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
            loadingText.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
            loadingText.setVisibility(View.GONE);

            adapter = new HorizontalAdapter(MoviesSectionActivity.this,"latest movies");

            MultiSnapRecyclerView firstRecyclerView = findViewById(R.id.first_recycler_view);
            MultiSnapRecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MoviesSectionActivity.this,LinearLayoutManager.HORIZONTAL,false);
            firstRecyclerView.setLayoutManager(layoutManager);
            firstRecyclerView.setAdapter(adapter);
            ItemClickSupport.addTo(firstRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                    String title = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.movie_title)).getText().toString();
                    Intent intent = new Intent(getApplicationContext(),SelectedMovieActivity.class);
                    intent.putExtra("title",title);
                    intent.putExtra("section","movie");
                    startActivity(intent);
                }
            });
            adapter1 = new HorizontalAdapter(MoviesSectionActivity.this,"latest series");

            MultiSnapRecyclerView secondRecyclerView = findViewById(R.id.second_recycler_view);
            MultiSnapRecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(MoviesSectionActivity.this,LinearLayoutManager.HORIZONTAL,false);
            secondRecyclerView.setLayoutManager(layoutManager1);
            secondRecyclerView.setAdapter(adapter1);
            ItemClickSupport.addTo(secondRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                    String title = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.movie_title)).getText().toString();
                    Intent intent = new Intent(getApplicationContext(),SelectedMovieActivity.class);
                    intent.putExtra("title",title);
                    intent.putExtra("section","tv");
                    startActivity(intent);
                }
            });

            floatingSearchViewSetup();
        }

    }
}
