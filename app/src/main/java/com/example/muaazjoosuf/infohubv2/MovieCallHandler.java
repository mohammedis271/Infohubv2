package com.example.muaazjoosuf.infohubv2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MovieCallHandler {

    private String API_KEY = "api_key=cc5bbb2749e2c1920594556fdbe02eb5";
    private String BASE_API = "https://api.themoviedb.org/3/";
    private String[] CALL_TYPE = {"movie?","tv?"};
    private String[] SEARCH_TYPE = {"discover/","find/"};

    public  ArrayList<String> title, poster_path ,
            keywords , casts, genre;
    private int page;

    public ArrayList<String> getTitle() {
        return title;
    }

    public void setTitle(ArrayList<String> title) {
        this.title = title;
    }

    public ArrayList<String> getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(ArrayList<String> poster_path) {
        this.poster_path = poster_path;
    }

    public MovieCallHandler(int page) {
        this.page = page;
    }

    public MovieCallHandler() {
    }


    public void discoverLatestMovies(String title_name,int callType){
        title = new ArrayList<>();
        poster_path = new ArrayList<>();
        DownloadTask downloadTask = new DownloadTask();

        String apiCall = BASE_API + SEARCH_TYPE[0] + CALL_TYPE[callType] + API_KEY +
                dateParameter(datePrevious(60),dateNow()) + "&" + sortBy(1);
        Log.i("CustomTag",apiCall);
        try {
            ArrayList<String> data = downloadTask.execute(apiCall).get();
            JSONObject object = new JSONObject(data.get(0));
            //logging(object.getString("page"));
            JSONArray jsonArray = new JSONArray(object.getString("results"));

            for(int i = 0;i<jsonArray.length();i++) {
                JSONObject jsonPart = jsonArray.getJSONObject(i);
                title.add(jsonPart.getString(title_name));
                poster_path.add(jsonPart.getString("poster_path"));
                Log.i("CustomTag",poster_path.get(i));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public String discoverLatestMoviesPosters(String poster_path){
        try{
            String imageCall = buildImageCall(1,"poster",4,poster_path);
            return imageCall;
        }catch (Exception e){
            e.printStackTrace();
            return null;
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



    private String dateParameter(String last_yyyymmdd, String start_yyyymmdd){
        return "&release_date.gte=" + last_yyyymmdd + "&release_date.lte=" + start_yyyymmdd;
    }

    private String dateNow(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return formatter.format(date);
    }

    private String datePrevious(int daysBefore){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate localDate = LocalDate.now().minusDays(daysBefore);
            return localDate.toString();
        }else{
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -daysBefore);
            return dateFormat.format(cal.getTime());
        }
    }

    private String sortBy(int number){
        String[] sort = {
                "popularity.asc",
                "popularity.desc",
                "release_date.asc",
                "release_date.desc",
                "vote_average.asc",
                "vote_average.desc",
                "vote_count.asc",
                "vote_count.desc"
        };
        return "&sort_by=" + sort[number];
    }

    public class DownloadTask extends AsyncTask<String, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(String... urls) {
            URL url;
            InputStream is;
            BufferedReader br;
            String line;
            ArrayList<String> html = new ArrayList<>();
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
