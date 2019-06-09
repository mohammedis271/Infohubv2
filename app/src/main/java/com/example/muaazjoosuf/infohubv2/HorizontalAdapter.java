package com.example.muaazjoosuf.infohubv2;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.felipecsl.gifimageview.library.GifImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.ViewHolder> {

    private Context c;
    private MovieCallHandler movieCallHandler;
    private byte[] bytes;
    private String section;

    public HorizontalAdapter(Context c, String section){
        this.c = c;
        movieCallHandler= new MovieCallHandler();
        switch (section){
            case "latest movies":
                movieCallHandler.discoverLatestMovies("title",0);
                this.section = section;
                break;
            case "latest series":
                movieCallHandler.discoverLatestMovies("name",1);
                this.section = section;
                break;
        }


        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(c.getApplicationContext()).build();
        ImageLoader.getInstance().init(config);

        RetrieveByteArray retrieveByteArray = new RetrieveByteArray();
        try {
              bytes = retrieveByteArray.execute("https://media2.giphy.com/media/VlJkP9Vxi4nkI/giphy.gif?cid=3640f6095c38cdbd30324f63515ed44b").get();

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }



    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horizontal,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        ImageLoader.getInstance()
                   .displayImage(movieCallHandler.discoverLatestMoviesPosters(movieCallHandler.poster_path.get(position)),
                           holder.movieImageView, new ImageLoadingListener() {
                               @Override
                               public void onLoadingStarted(String imageUri, View view) {
                                   holder.gifImageView.setBytes(bytes);
                                   holder.movieImageView.setVisibility(View.INVISIBLE);
                                   holder.gifImageView.setVisibility(View.VISIBLE);
                                   holder.gifImageView.startAnimation();
                               }

                               @Override
                               public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                               }

                               @Override
                               public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                   holder.gifImageView.setBytes(bytes);
                                   holder.gifImageView.stopAnimation();
                                   holder.gifImageView.setVisibility(View.GONE);
                                   holder.movieImageView.setVisibility(View.VISIBLE);
                               }

                               @Override
                               public void onLoadingCancelled(String imageUri, View view) {

                               }
                           });
        holder.movieTitleTextView.setText(movieCallHandler.title.get(position));
    }

    @Override
    public int getItemCount() {
        return movieCallHandler.title.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView movieTitleTextView;
        ImageView movieImageView;
        GifImageView gifImageView;

        ViewHolder(View itemView){
            super(itemView);
            movieTitleTextView = itemView.findViewById(R.id.movie_title);
            movieImageView = itemView.findViewById(R.id.movie_img);
            gifImageView = itemView.findViewById(R.id.gifImageView);
        }
    }

    class RetrieveByteArray extends AsyncTask<String,Void,byte[]> {

        @Override
        protected byte[] doInBackground(String... strings) {
            try{
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if(urlConnection.getResponseCode() == 200){//200 = ok
                    InputStream in  = new BufferedInputStream(urlConnection.getInputStream());
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    int nRead;
                    byte[] data = new byte[10240];
                    while ((nRead = in.read(data,0,data.length)) != -1){
                        buffer.write(data,0,nRead);
                    }
                    buffer.flush();
                    return buffer.toByteArray();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
