package com.example.muaazjoosuf.infohubv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.takusemba.multisnaprecyclerview.MultiSnapRecyclerView;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class SelectedMovieActivity extends AppCompatActivity {



    private SelectedMovieCallHandler sm;
    private String query,section;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_movie);
        Intent intent = getIntent();
        query = intent.getStringExtra("title");
        section = intent.getStringExtra("section");

        //draws all relevant information about query self built class
        sm = new SelectedMovieCallHandler(query,section);
        sm.sortJSONDataIntoArrays();
        sm.fetchMovieReviews();

        KenBurnsView background = findViewById(R.id.drop_background);
        ImageDownloader imageDownloader = new ImageDownloader();
        try {
            String call = buildImageCall(1,"back drop",1,sm.getBackdrop_path());
            Bitmap bitmap = imageDownloader.execute(call).get();
            background.setImageBitmap(bitmap);
        }catch (Exception e){e.printStackTrace();}

        new MyTask().execute();

        //back button on top of the screen
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
    }

    private void buildFirstCard(){
        TextView movieTitle = findViewById(R.id.card1_heading_movie_name);
        if(sm.getTitle().equals(sm.getOriginal_title())){
            movieTitle.setText(sm.getTitle());
        }else{
            String string = sm.getOriginal_title() + " (" + sm.getTitle() + ")";
            movieTitle.setText(string);
        }


        TextView ratingDurationGenre = findViewById(R.id.card1_rating_duration_genre);
        String combined;
        if(section.equals("movie")){
            combined = "rating: " + sm.getVote_average() + " | " + sm.getRuntime() + " min | ";
            for (int i = 0; i<sm.getGenreNames().size() - 1;i++){
                combined += sm.getGenreNames().get(i) + ", ";
            }
            combined = combined.trim();
            combined = combined.substring(0,(combined.length()-1));
            combined += " |";
            ratingDurationGenre.setText(combined);
        }else{
            combined = "rating: " + sm.getVote_average() + " | " + sm.getRuntime() + " | ";
            for (int i = 0; i<sm.getGenreNames().size() - 1;i++){
                combined += sm.getGenreNames().get(i) + ", ";
            }
            combined = combined.trim();
            combined = combined.substring(0,(combined.length()-1));
            combined += " |";
            ratingDurationGenre.setText(combined);
        }


        ImageView poster = findViewById(R.id.card1_poster_image_view);
        String call = buildImageCall(1,"poster",6,sm.getPoster_path());
        ImageDownloader imageDownloader = new ImageDownloader();
        try{
            Bitmap bitmap = imageDownloader.execute(call).get();
            poster.setImageBitmap(bitmap);
        }catch (Exception e){e.printStackTrace();}

        TextView overviewText = findViewById(R.id.card1_overview_text_view);
        if(sm.getOverview().length()>230){
            String item = sm.getOverview();
            item = item.substring(0,230);
            item += " ...";
            overviewText.setText(item);
        }else{
            overviewText.setText(sm.getOverview());
        }


    }

    private void buildSecondCard(){
        MultiSnapRecyclerView.Adapter adapter = new HorizontalAdapterCasts(this,query,section);
        MultiSnapRecyclerView firstRecyclerView = findViewById(R.id.card2_recycler_view);
        MultiSnapRecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        firstRecyclerView.setLayoutManager(layoutManager);
        firstRecyclerView.setAdapter(adapter);
    }

    private void buildThirdCard(){

        YouTubePlayerFragment youTubePlayerFragment =
                (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_fragment);
        youTubePlayerFragment.initialize("AIzaSyDUV3oJCWkdmVEzfuvSDMOn-A_svtxFMgQ",
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                            YouTubePlayer youTubePlayer, boolean b) {

                        // do any work here to cue video, play video, etc.
                        youTubePlayer.cueVideo(sm.getTrailerKey().get(0));
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                            YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });
        TextView textView = findViewById(R.id.card3_trailer_name);
        textView.setText(sm.getTrailerName().get(0));


    }

    private void buildFourthCard(){
        TextView authorText = (TextView) findViewById(R.id.card4_author) ;
        TextView content = findViewById(R.id.card4_content);
        TextView seeAllBtn = findViewById(R.id.see_all_card4);
        if (sm.getReviewAuthor().isEmpty()){
            authorText.setText("No Reviews yet");
            content.setText("Sorry");
            seeAllBtn.setEnabled(false);
        }else{

            authorText.setText(sm.getReviewAuthor().get(0));

            if(sm.getReviewContent().get(0).length()>230){
                String item = sm.getReviewContent().get(0);
                item = item.substring(0,230);
                item += " ...";
                content.setText(item);
            }else {
                content.setText(sm.getReviewContent().get(0));
            }
        }
    }

    private String buildImageCall(int secureConnection, String type,int sizeNumber, String imgPath){
        String[] baseURl = {"http://image.tmdb.org/t/p/","https://image.tmdb.org/t/p/"};
        String size = "";
        switch (type) {
            case "back drop":
                switch (sizeNumber) {
                    case 0:
                        size = "w300";
                        break;
                    case 1:
                        size = "w780";
                        break;
                    case 2:
                        size = "w1280";
                        break;
                    case 3:
                        size = "original";
                        break;
                }
                break;
            case "logo":
                switch (sizeNumber) {
                    case 0:
                        size = "w45";
                        break;
                    case 1:
                        size = "w92";
                        break;
                    case 2:
                        size = "w154";
                        break;
                    case 3:
                        size = "w185";
                        break;
                    case 4:
                        size = "w300";
                        break;
                    case 5:
                        size = "w500";
                        break;
                    case 6:
                        size = "original";
                        break;
                }

                break;
            case "poster":
                switch (sizeNumber) {
                    case 0:
                        size = "w92";
                        break;
                    case 1:
                        size = "w154";
                        break;
                    case 2:
                        size = "w185";
                        break;
                    case 3:
                        size = "w342";
                        break;
                    case 4:
                        size = "w500";
                        break;
                    case 5:
                        size = "w780";
                        break;
                    case 6:
                        size = "original";
                        break;
                }

                break;
            case "profile":
                switch (sizeNumber) {
                    case 0:
                        size = "w45";
                        break;
                    case 1:
                        size = "w185";
                        break;
                    case 2:
                        size = "h632";
                        break;
                    case 3:
                        size = "original";
                        break;
                }

                break;
            default:
                return null;
        }
        String imageCall = baseURl[secureConnection] + size + imgPath;
        return imageCall;
    }

    //Button to make sure that when back button is pressed it exists intent
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void seeAllCastAndCrew(View view){
        Intent intent = new Intent(getApplicationContext(),SelectedMovieCastsPage.class);
        intent.putExtra("query",sm.getTitle());
        intent.putExtra("section",section);
        startActivity(intent);
    }

    public void seeAllTrailers(View view){
        Intent intent = new Intent(getApplicationContext(),SelectedMovieTrailerPage.class);
        intent.putExtra("query",sm.getTitle());
        intent.putExtra("section",section);
        startActivity(intent);
    }

    public void seeAllReviews(View view) {
        Intent intent = new Intent(getApplicationContext(),SelectedMovieReviewPage.class);
        intent.putExtra("query",sm.getTitle());
        intent.putExtra("section",section);
        startActivity(intent);
    }

    public void goToHomeWebsite(View view){
        if (sm.getHomepage().isEmpty()){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.imdb.com/"));
            startActivity(browserIntent);
        }else {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(sm.getHomepage()));
            startActivity(browserIntent);
        }
    }



    public class ImageDownloader extends AsyncTask<String,Void,Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            try{
                URL url = new URL(urls[0]);
                HttpURLConnection myConnection =(HttpURLConnection) url.openConnection();
                myConnection.connect();
                InputStream inputStream = myConnection.getInputStream();
                Bitmap myImage = BitmapFactory.decodeStream(inputStream);
                return myImage;
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
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
            LinearLayout linearLayout = findViewById(R.id.card_linear_layout);
            linearLayout.setVisibility(View.GONE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            LinearLayout linearLayout = findViewById(R.id.card_linear_layout);
            linearLayout.setVisibility(View.VISIBLE);
            buildFirstCard();
            buildSecondCard();
            buildThirdCard();
            buildFourthCard();


        }

    }
}
