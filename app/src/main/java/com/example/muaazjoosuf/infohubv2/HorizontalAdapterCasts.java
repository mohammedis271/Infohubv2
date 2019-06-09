package com.example.muaazjoosuf.infohubv2;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HorizontalAdapterCasts extends RecyclerView.Adapter<HorizontalAdapterCasts.ViewHolder>{

    String query;
    SelectedMovieCallHandler sm;
    Context c;

    public HorizontalAdapterCasts(Context c,String query,String section){
        this.c = c;
        this.query = query;
        sm = new SelectedMovieCallHandler(query,section);
        sm.sortJSONDataIntoArrays();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(c.getApplicationContext()).build();
        ImageLoader.getInstance().init(config);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horizontal_cast,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        String call = buildImageCall(1,"profile",1,sm.getCastProfilePaths().get(position));
        ImageLoader.getInstance()
                   .displayImage(call, holder.posterImage, new ImageLoadingListener() {
                       @Override
                       public void onLoadingStarted(String imageUri, View view) {
                           holder.posterImage.setImageResource(R.drawable.camera_loading_icon);
                       }

                       @Override
                       public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                       }

                       @Override
                       public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                           holder.posterImage.setImageBitmap(loadedImage);
                       }

                       @Override
                       public void onLoadingCancelled(String imageUri, View view) {

                       }
                   });
        holder.characterText.setText(sm.getCastCharacters().get(position));
        holder.nameText.setText(sm.getCastNames().get(position));

    }

    @Override
    public int getItemCount() {

        return sm.getCastNames().size();

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

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameText,characterText;
        ImageView posterImage;

        ViewHolder(View itemView){
            super(itemView);
            nameText = itemView.findViewById(R.id.cast_actual_name);
            characterText = itemView.findViewById(R.id.cast_character_name);
            posterImage = itemView.findViewById(R.id.cast_profile_img);

        }
    }
}
