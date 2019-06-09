package com.example.muaazjoosuf.infohubv2;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class SearchMovieCallHandler{

    private String API_KEY = "api_key=cc5bbb2749e2c1920594556fdbe02eb5";
    private String BASE_API = "https://api.themoviedb.org/3/";
    private String[] CALL_TYPE = {"movie?","tv?"};
    private String[] SEARCH_TYPE = {"search/","find/"};

    private String query,section;

    protected ArrayList<String> titles,poster_path,overview,finalPosterCall;

    protected ArrayList<String> titlesSeries,poster_pathSeries,overviewSeries,finalPosterCallSeries;


    public SearchMovieCallHandler(String query,String section){
        this.query = query;
        this.section = section;

    }

    public ArrayList<String> getTitles() {
        return titles;
    }

    public ArrayList<String> getFinalPosterCall() {
        return finalPosterCall;
    }

    public ArrayList<String> getOverviewSeries() {
        return overviewSeries;
    }

    public ArrayList<String> getFinalPosterCallSeries() {
        return finalPosterCallSeries;
    }

    public ArrayList<String> getTitlesSeries() {
        return titlesSeries;
    }

    public ArrayList<String> getOverview() {
        return overview;
    }

    private void arrayInitialise(){
        titles = new ArrayList<>();
        poster_path = new ArrayList<>();
        overview = new ArrayList<>();
        finalPosterCall = new ArrayList<>();
    }

    private void arrayInitialiseSeries(){
        titlesSeries = new ArrayList<>();
        poster_pathSeries = new ArrayList<>();
        overviewSeries = new ArrayList<>();
        finalPosterCallSeries = new ArrayList<>();
    }

    private String buildMovieApiCall(){
        return BASE_API + SEARCH_TYPE[0] + CALL_TYPE[0] + API_KEY +
                "&language=en-US&query=" + query + "&page=1&include_adult=false";
    }

    private String buildSeriesApiCall(){
        return BASE_API + SEARCH_TYPE[0] + CALL_TYPE[1] + API_KEY +
                "&language=en-US&query=" + query + "&page=1&include_adult=false";
    }

    protected void SearchLatestMovie(){
        arrayInitialise();
        DownloadTask downloadTask = new DownloadTask();
        try {
            ArrayList<String> arrayList = downloadTask.execute(buildMovieApiCall()).get();

            JSONObject jsonObject = new JSONObject(arrayList.get(0));
            JSONArray jsonArray = new JSONArray(jsonObject.getString("results"));

            for(int i = 0; i<jsonArray.length();i++){
                JSONObject part = jsonArray.getJSONObject(i);
                titles.add(part.getString("title"));
                poster_path.add(part.getString("poster_path"));
                overview.add(part.getString("overview"));
                finalPosterCall.add(buildImageCall(1,"poster", 5,poster_path.get(i)));

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void SearchLatestSeries(){
        arrayInitialiseSeries();
        DownloadTask downloadTask = new DownloadTask();
        try{
            ArrayList<String> arrayList = downloadTask.execute(buildSeriesApiCall()).get();

            JSONObject jsonObject = new JSONObject(arrayList.get(0));
            JSONArray jsonArray = new JSONArray(jsonObject.getString("results"));

            for (int i = 0; i<jsonArray.length();i++){
                JSONObject part = jsonArray.getJSONObject(i);
                titlesSeries.add(part.getString("name"));
                overviewSeries.add(part.getString("overview"));
                poster_pathSeries.add(part.getString("poster_path"));
                finalPosterCallSeries.add(buildImageCall(1,"poster",5,poster_pathSeries.get(i)));
            }
        }catch (Exception e){
            e.printStackTrace();
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


    private class DownloadTask extends AsyncTask<String, Void, ArrayList<String>> {
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
