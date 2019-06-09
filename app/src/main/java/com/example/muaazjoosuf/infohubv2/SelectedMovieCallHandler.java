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

public class SelectedMovieCallHandler {
    private String API_KEY = "api_key=cc5bbb2749e2c1920594556fdbe02eb5";
    private String BASE_API = "https://api.themoviedb.org/3/";
    private String[] CALL_TYPE = {"movie?", "tv?"};
    private String[] SEARCH_TYPE = {"discover/", "find/", "search/"};

    private ArrayList<String> ids = new ArrayList<>();
    private ArrayList<String> titles = new ArrayList<>();

    private String query, movieId, section;

    protected ArrayList<String> trailerKey, trailerName, genreNames, castNames,
            castCharacters, castProfilePaths,reviewAuthor, reviewContent,crewNames,
            crewCharacters, crewProfilePaths;

    protected String backdrop_path, homepage, original_title,
            title, overview, poster_path, release_date, runtime, vote_average;

    protected boolean reviewCheck;


    public SelectedMovieCallHandler(String query,String section) {
        this.section = section;
        this.query = query;
    }

    public ArrayList<String> getCrewNames() {
        return crewNames;
    }

    public ArrayList<String> getCrewCharacters() {
        return crewCharacters;
    }

    public ArrayList<String> getCrewProfilePaths() {
        return crewProfilePaths;
    }

    public ArrayList<String> getReviewAuthor() {
        return reviewAuthor;
    }

    public ArrayList<String> getReviewContent() {
        return reviewContent;
    }

    public boolean isReviewCheck() {
        return reviewCheck;
    }

    public ArrayList<String> getTrailerKey() {
        return trailerKey;
    }

    public ArrayList<String> getTrailerName() {
        return trailerName;
    }

    public ArrayList<String> getGenreNames() {
        return genreNames;
    }

    public ArrayList<String> getCastNames() {
        return castNames;
    }

    public ArrayList<String> getCastCharacters() {
        return castCharacters;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getHomepage() {
        return homepage;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getVote_average() {
        return vote_average;
    }

    public ArrayList<String> getCastProfilePaths() {
        return castProfilePaths;
    }
    // My code

    private String buildApiCall() {
        if(section.equals("movie")){
            return BASE_API + SEARCH_TYPE[2] + CALL_TYPE[0] + API_KEY + "&query=" + query;
        }else{
            return BASE_API + SEARCH_TYPE[2] + CALL_TYPE[1] + API_KEY + "&query=" + query;
        }
    }

    private String buildFinalApiCall(String finalId) {
        if(section.equals("movie")){
            return BASE_API + "movie/" + finalId + "?" + API_KEY + "&append_to_response=casts,videos";
        }else{
            logging(BASE_API + "tv/" + finalId + "?" + API_KEY + "&append_to_response=casts,videos");
            return BASE_API + "tv/" + finalId + "?" + API_KEY + "&append_to_response=credits,videos";
        }

    }

    private String fetchJSONFilesPerClickedMovie() {
        DownloadTask downloadTask = new DownloadTask();

        try {
            ArrayList<String> arrayList = downloadTask.execute(buildApiCall()).get();

            JSONObject jsonObject = new JSONObject(arrayList.get(0));
            JSONArray jsonArray = new JSONArray(jsonObject.getString("results"));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject part = jsonArray.getJSONObject(i);
                ids.add(part.getString("id"));
                if(section.equals("movie")){
                    titles.add(part.getString("title"));
                }else {
                    titles.add(part.getString("name"));
                }

            }
            String finalId = "";
            for (int i = 0; i < ids.size(); i++) {
                if (titles.get(i).equals(query)) {
                    finalId = ids.get(i);
                    movieId = finalId;
                    break;
                }
            }
            return buildFinalApiCall(finalId);


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    private void arrayInitialiser() {
        trailerKey = new ArrayList<>();
        trailerName = new ArrayList<>();
        genreNames = new ArrayList<>();
        castNames = new ArrayList<>();
        castCharacters = new ArrayList<>();
        castProfilePaths = new ArrayList<>();
        reviewAuthor= new ArrayList<>();
        reviewContent = new ArrayList<>();
        crewCharacters = new ArrayList<>();
        crewNames = new ArrayList<>();
        crewProfilePaths = new ArrayList<>();
    }


    protected void sortJSONDataIntoArrays() {
        if(section.equals("movie")){
            sortingMovieCalls();
        }else{
            sortingTvCalls();
        }
    }

    private void sortingMovieCalls(){
        try {
            String finalApiCall = fetchJSONFilesPerClickedMovie();
            logging(finalApiCall);
            DownloadTask task = new DownloadTask();
            ArrayList<String> finalArrayList = task.execute(finalApiCall).get();
            JSONObject object = new JSONObject(finalArrayList.get(0));

            backdrop_path = (object.getString("backdrop_path"));
            homepage = (object.getString("homepage"));
            original_title = (object.getString("original_title"));
            title = (object.getString("title"));
            overview = (object.getString("overview"));
            poster_path = (object.getString("poster_path"));
            release_date = (object.getString("release_date"));
            runtime = (object.getString("runtime"));
            vote_average = (object.getString("vote_average"));

            arrayInitialiser();

            String videos = object.getString("videos");
            JSONObject videoList = new JSONObject(videos);
            JSONArray videoArray = new JSONArray(videoList.getString("results"));
            for (int i = 0; i < videoArray.length(); i++) {
                JSONObject part = videoArray.getJSONObject(i);
                trailerKey.add(part.getString("key"));
                trailerName.add(part.getString("name"));
            }

            String genres = object.getString("genres");
            JSONArray genreArray = new JSONArray(genres);
            for (int i = 0; i < genreArray.length(); i++) {
                JSONObject part = genreArray.getJSONObject(i);
                genreNames.add(part.getString("name"));
            }

            String casts = object.getString("casts");
            JSONArray castsArray = new JSONArray(new JSONObject(casts).getString("cast"));
            for (int i = 0; i < castsArray.length(); i++) {
                JSONObject part = castsArray.getJSONObject(i);
                castNames.add(part.getString("name"));
                castCharacters.add(part.getString("character"));
                castProfilePaths.add(part.getString("profile_path"));
            }

            JSONArray crewArray = new JSONArray(new JSONObject(casts).getString("crew"));
            for (int i = 0; i < crewArray.length(); i++) {
                JSONObject part = crewArray.getJSONObject(i);
                crewNames.add(part.getString("name"));
                crewCharacters.add(part.getString("department"));
                crewProfilePaths.add(part.getString("profile_path"));
                logging(crewNames.get(i));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sortingTvCalls(){
        try {
            String finalApiCall = fetchJSONFilesPerClickedMovie();
            logging(finalApiCall);
            DownloadTask task = new DownloadTask();
            ArrayList<String> finalArrayList = task.execute(finalApiCall).get();
            JSONObject object = new JSONObject(finalArrayList.get(0));

            backdrop_path = (object.getString("backdrop_path"));
            homepage = (object.getString("homepage"));
            original_title = (object.getString("original_name"));
            title = (object.getString("name"));
            overview = (object.getString("overview"));
            poster_path = (object.getString("poster_path"));
            vote_average = (object.getString("vote_average"));
            runtime = "Episodes: " + object.getString("number_of_episodes")
                    +" Seasons: " + object.getString("number_of_seasons");

            logging(backdrop_path);
            logging(homepage);
            logging(original_title);
            logging(title);
            logging(overview);
            logging(poster_path);
            logging(vote_average);

            arrayInitialiser();

            String videos = object.getString("videos");
            JSONObject videoList = new JSONObject(videos);
            JSONArray videoArray = new JSONArray(videoList.getString("results"));
            for (int i = 0; i < videoArray.length(); i++) {
                JSONObject part = videoArray.getJSONObject(i);
                trailerKey.add(part.getString("key"));
                trailerName.add(part.getString("name"));
                logging(trailerKey.get(i));
                logging(trailerName.get(i));
            }

            String genres = object.getString("genres");
            JSONArray genreArray = new JSONArray(genres);
            for (int i = 0; i < genreArray.length(); i++) {
                JSONObject part = genreArray.getJSONObject(i);
                genreNames.add(part.getString("name"));
                logging(genreNames.get(i));
            }

            String casts = object.getString("credits");
            JSONArray castsArray = new JSONArray(new JSONObject(casts).getString("cast"));
            for (int i = 0; i < castsArray.length(); i++) {
                JSONObject part = castsArray.getJSONObject(i);
                castNames.add(part.getString("name"));
                castCharacters.add(part.getString("character"));
                castProfilePaths.add(part.getString("profile_path"));
                logging(castNames.get(i));
                logging(castCharacters.get(i));
                logging(castProfilePaths.get(i));
            }

            JSONArray crewArray = new JSONArray(new JSONObject(casts).getString("crew"));
            for (int i = 0; i < crewArray.length(); i++) {
                JSONObject part = crewArray.getJSONObject(i);
                crewNames.add(part.getString("name"));
                crewCharacters.add(part.getString("department"));
                crewProfilePaths.add(part.getString("profile_path"));
                logging(crewNames.get(i));
                logging(crewCharacters.get(i));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void fetchMovieReviews() {
        DownloadTask task = new DownloadTask();
        try {
            String apiCall;
            if (section.equals("movie")) {
                apiCall = BASE_API + "movie/" + movieId + "/reviews?"
                        + API_KEY;
            }else{
                apiCall = BASE_API + "tv/" + movieId + "/reviews?"
                        + API_KEY;
            }
            ArrayList<String> call = task.execute(apiCall).get();
            JSONObject object = new JSONObject(call.get(0));
            JSONArray array = new JSONArray(object.getString("results"));

            if (array.length() > 0) {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject part = array.getJSONObject(i);
                    reviewAuthor.add(part.getString("author"));
                    reviewContent.add(part.getString("content"));
                }
                reviewCheck = true;
            } else {
                reviewCheck = false;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void logging(String string) {
        Log.i("CustomTag", string);
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
