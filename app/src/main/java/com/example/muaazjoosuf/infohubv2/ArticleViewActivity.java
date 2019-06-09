package com.example.muaazjoosuf.infohubv2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class ArticleViewActivity extends AppCompatActivity {

    public String urlToLoad,websiteTitle;
    public FloatingActionMenu floatingActionMenu;
    public FloatingActionButton saveFab;

    public static SQLiteDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_view);

        database = this.openOrCreateDatabase("SavedWebsites",MODE_PRIVATE,null);
        database.execSQL("CREATE TABLE IF NOT EXISTS SavedHtml (id INTEGER PRIMARY KEY AUTOINCREMENT, Title VARCHAR, html VARCHAR,url VARCHAR)");



        Intent intent = getIntent();
        urlToLoad = intent.getStringExtra("websiteLink");
        websiteTitle = intent.getStringExtra("websiteTitle");
        WebView webView = findViewById(R.id.webViewContent);
        webView.loadUrl(urlToLoad);

        saveFab = findViewById(R.id.saveWebsiteFab);
        saveFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveHtml();
                View parentLayout = findViewById(android.R.id.content);
                Snackbar.make(parentLayout, "Saved", Snackbar.LENGTH_LONG)
                        .setAction("Close", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setActionTextColor(getResources().getColor(R.color.white ))
                        .show();
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
    }


    //Button to make sure that when back button is pressed it exists intent
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void addEntry( String title, String html,String url) throws SQLiteException {
        ContentValues cv = new  ContentValues();
        cv.put("Title", title);
        cv.put("html", html);
        cv.put("url",url);
        database.insert("SavedHtml", null, cv );
    }

    public void saveHtml(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                try{
                    DownloadHTML task = new DownloadHTML();
                    ArrayList<String> arrayList = task.execute(urlToLoad).get();
                    StringBuilder result = new StringBuilder();
                    for(String item : arrayList){
                        result.append(item);
                    }
                    String finalResult = result.toString();

                    addEntry(websiteTitle,finalResult,urlToLoad);

                    Cursor cursor = database.rawQuery("SELECT * FROM SavedHtml",null);

                    int idIndex = cursor.getColumnIndex("id");
                    int titleIndex = cursor.getColumnIndex("Title");
                    int htmlIndex = cursor.getColumnIndex("html");

                    cursor.moveToFirst();
                    while (cursor!= null){
                        Log.i("CustomTag -id",Integer.toString(cursor.getInt(idIndex)));
                        Log.i("CustomTag -title",cursor.getString(titleIndex));
                        Log.i("CustomTag -html",cursor.getString(htmlIndex));
                        cursor.moveToNext();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public class DownloadHTML extends AsyncTask<String,Void,ArrayList> {
        @Override
        protected ArrayList doInBackground(String... urls) {
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
}
