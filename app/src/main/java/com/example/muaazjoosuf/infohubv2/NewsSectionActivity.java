package com.example.muaazjoosuf.infohubv2;

import  androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;

import android.view.animation.AlphaAnimation;

import android.widget.FrameLayout;

import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewsSectionActivity extends AppCompatActivity {


    //MY IMPORTS
    public ArrayList<String> titleArray = new ArrayList<>();
    public ArrayList<String> imgArray = new ArrayList<>();
    public ArrayList<String> descriptionArray = new ArrayList<>();
    public ArrayList<String> urlToWebsiteArray = new ArrayList<>();
    public  ArrayList<NewsArticle> newsArticles;

    public RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;
    public RecyclerView.LayoutManager layoutManager;

    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    FrameLayout progressBarHolder;
    TextView loadingText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_section);

        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
        loadingText = findViewById(R.id.loading_text);

        //move back-button to the top left of the screen
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);


        //MY CODE
        Intent intent = getIntent();
        String transferData = intent.getStringExtra("transferData");
        if(transferData!=null){
            Log.i("transferData",transferData);
            final String call = generateAPICall(transferData);
            Log.i("CustomTag",call);

            DownloadTask task = new DownloadTask();
            try {
                ArrayList<String> jsonData = task.execute(call).get();

                JSONObject object = new JSONObject(jsonData.get(0));
                JSONArray jsonArray = new JSONArray(object.getString("articles"));

                for(int i = 0;i<jsonArray.length();i++){
                    JSONObject jsonPart = jsonArray.getJSONObject(i);
                    String title = jsonPart.getString("title");
                    String description = jsonPart.getString("description");
                    String url = jsonPart.getString("url");
                    String urlToImage = jsonPart.getString("urlToImage");
                    String dateOfPublication = jsonPart.getString("publishedAt");
                    String content = jsonPart.getString("content");

                    titleArray.add(title);
                    descriptionArray.add(description);
                    imgArray.add(urlToImage);
                    urlToWebsiteArray.add(url);
                    Log.i("CustomTag","OK");
                }
                new MyTask().execute();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    //Button to make sure that when back button is pressed it exists intent
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    //INFOHUB CODE

    //String manipulation of JSON to API REST Call
    public String generateAPICall(String rawData){
        String finalApiCall = "https://newsapi.org/v2/";
        String[] parts = rawData.split("_");

        if (parts[0].equals("Everything")){
            parts[3] = "";
            finalApiCall = finalApiCall + parts[0].toLowerCase().trim() + "?";


            if(!parts[2].isEmpty()){
                finalApiCall+= "&sortby=" +parts[2];
            }

            if(!parts[4].isEmpty()){
                String[] split = parts[4].split("\\s+");
                if (split.length!=1){
                    for(String keyword :split){
                        finalApiCall+= "&q=" + keyword;
                    }
                }else{
                    finalApiCall+="&q=" + parts[4];
                }
            }
            if(!parts[5].isEmpty()){
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                finalApiCall += "&from=" +parts[5] +"&to=" + formatter.format(date);
            }

            finalApiCall += "&apiKey=817d7ac1831c42edbd36b1562d71ef24";
            finalApiCall = finalApiCall.replace("?&","?");

        }else if(parts[0].equals("Top Headlines")){
            finalApiCall = finalApiCall + "top-headlines?";
            if(!parts[1].isEmpty()){
                Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(parts[1]);
                while (m.find()) {
                    finalApiCall = finalApiCall + "&language=" + m.group(1).toLowerCase().trim();
                }
            }
            if(!parts[2].isEmpty()){
                finalApiCall+= "&sortby=" +parts[2];
            }
            if(!parts[3].isEmpty()){
                Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(parts[3]);
                while (m.find()) {
                    finalApiCall += "&country=" +m.group(1).toLowerCase().trim();
                }
            }
            if(!parts[4].isEmpty()){
                String[] split = parts[4].split("\\s+");
                if (split.length!=1){
                    for(String keyword :split){
                        finalApiCall+= "&q=" + keyword;
                    }
                }else{
                    finalApiCall+="&q=" + parts[4];
                }
            }
            if(!parts[5].isEmpty()){
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                finalApiCall += "&from=" +parts[5] +"&to=" + formatter.format(date);
            }

            finalApiCall += "&apiKey=817d7ac1831c42edbd36b1562d71ef24";
            finalApiCall = finalApiCall.replace("?&","?").toLowerCase().trim();
        }
        return finalApiCall;
    }

    //creation of menu search item
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //menu search icon
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.search){
            Intent intent = new Intent(getApplicationContext(), NewsSearchActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return false;
    }

    //download content
    public class DownloadTask extends AsyncTask<String, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(String... urls) {
            URL url;
            InputStream is;
            BufferedReader br;
            String line;
            ArrayList<String> html = new ArrayList<String>();
            try {
                url = new URL(urls[0]);
                is = url.openStream();  // throws an IOException
                br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    html.add(line.trim());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return html;
        }
    }

    //image downloader
    public class ImageDownloader extends AsyncTask<String,Void,Bitmap>{
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
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
            loadingText.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
            loadingText.setVisibility(View.GONE);

            int count = 0;
            ArrayList<NewsArticle> newsArticles = new ArrayList<>();
            for(String N:titleArray){
                ImageDownloader imageDownloader = new ImageDownloader();

                try {
                    Bitmap bm = imageDownloader.execute(imgArray.get(count)).get();
                    if(bm!=null){
                        NewsArticle newsArticle = new NewsArticle(N,descriptionArray.get(count),bm);
                        newsArticles.add(newsArticle);
                    }else{
                        NewsArticle newsArticle = new NewsArticle(N,descriptionArray.get(count),null);
                        newsArticles.add(newsArticle);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                count++;

            }

            recyclerView = findViewById(R.id.recycler_view);
            layoutManager = new LinearLayoutManager(NewsSectionActivity.this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            adapter = new NewsArticleAdapter(NewsSectionActivity.this,newsArticles);
            recyclerView.setAdapter(adapter);
            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(NewsSectionActivity.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent(getApplicationContext(),ArticleViewActivity.class);
                    intent.putExtra("websiteLink",urlToWebsiteArray.get(position));
                    intent.putExtra("websiteTitle",titleArray.get(position));
                    startActivity(intent);

                }

                @Override
                public void onLongItemClick(View view, int position) {
                    //long click to share article !!!!!!!!!!!!!!!
                }
            }));

            //no content control
            if (newsArticles.isEmpty()){
                recyclerView.setVisibility(View.GONE);
                TextView noContentTextView = findViewById(R.id.No_Content_Notification_Text);
                noContentTextView.setVisibility(View.VISIBLE);
            }else{
                recyclerView.setVisibility(View.VISIBLE);
                TextView noContentTextView = findViewById(R.id.No_Content_Notification_Text);
                noContentTextView.setVisibility(View.GONE);
            }

        }

    }




}
