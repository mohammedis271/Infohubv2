package com.example.muaazjoosuf.infohubv2;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.muaazjoosuf.infohubv2.ArticleViewActivity.database;

public class OfflineActivity extends AppCompatActivity {


    ArrayList<String> savedWebsites;
    ArrayList<String> savedTitles;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);

        doAll();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void doAll(){
        final ListView listView = (ListView)findViewById(R.id.offline_list_view);
        final WebView webView = (WebView)findViewById(R.id.offline_web_view);
        TextView textView = (TextView)findViewById(R.id.offline_text_view);
        savedWebsites = new ArrayList<>();
        savedTitles = new ArrayList<>();

        try{
            database = this.openOrCreateDatabase("SavedWebsites",MODE_PRIVATE,null);
            database.execSQL("CREATE TABLE IF NOT EXISTS SavedHtml (id INTEGER PRIMARY KEY AUTOINCREMENT, Title VARCHAR, html VARCHAR)");
            Cursor cursor = database.rawQuery("SELECT * FROM SavedHtml",null);

            int titleIndex = cursor.getColumnIndex("Title");
            int htmlIndex = cursor.getColumnIndex("html");

            cursor.moveToFirst();
            while (cursor!= null){
                Log.i("CustomTag -title",cursor.getString(titleIndex));
                Log.i("CustomTag -html",cursor.getString(htmlIndex));
                savedWebsites.add(cursor.getString(htmlIndex));
                savedTitles.add(cursor.getString(titleIndex));
                cursor.moveToNext();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,savedTitles);
        listView.setAdapter(adapter);
        if(adapter.getCount()==0){
            listView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }else {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    listView.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.loadData(savedWebsites.get(position), "text/html", "UTF-8");
                }
            });
        }

    }
}
