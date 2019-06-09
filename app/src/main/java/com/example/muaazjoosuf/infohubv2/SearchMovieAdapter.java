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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import androidx.recyclerview.widget.RecyclerView;

public class SearchMovieAdapter extends RecyclerView.Adapter<SearchMovieAdapter.ViewHolder> {


    SearchMovieCallHandler sm;
    Context context;
    String query,section;
    ArrayList<CombinedResults> combinedResults;

    public SearchMovieAdapter(Context context, String query,String section){
        this.query = query;
        this.section = section;
        this.context = context;
        sm = new SearchMovieCallHandler(query,section);
        sm.SearchLatestMovie();
        sm.SearchLatestSeries();
        combinedResults = randomizeResults();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context.getApplicationContext()).build();
        ImageLoader.getInstance().init(config);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_results_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.heading.setText(combinedResults.get(position).getTitle());
        holder.content.setText(combinedResults.get(position).getOverview());
        ImageLoader.getInstance()
                   .displayImage(combinedResults.get(position).getPosterCall(),
                           holder.poster, new ImageLoadingListener() {
                               @Override
                               public void onLoadingStarted(String imageUri, View view) {
                                   holder.poster.setImageResource(R.drawable.ic_launcher_background);
                               }

                               @Override
                               public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                               }

                               @Override
                               public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                               }

                               @Override
                               public void onLoadingCancelled(String imageUri, View view) {

                               }
                           });
        holder.heading.setTag(combinedResults.get(position).getTag());
    }

    private class CombinedResults{
        private String title,posterCall,overview;
        private int tag;
        protected CombinedResults(String title,String posterCall, String overview,int tag){
            this.overview = overview;
            this.posterCall = posterCall;
            this.title = title;
            this.tag = tag;
        }

        public int getTag() {
            return tag;
        }

        public String getTitle() {
            return title;
        }

        public String getPosterCall() {
            return posterCall;
        }

        public String getOverview() {
            return overview;
        }
    }

    private ArrayList<CombinedResults> randomizeResults(){
        ArrayList<CombinedResults> combinedResults = new ArrayList<>();

        if (sm.getTitles() != null && sm.getTitles().size()>0){
            for(int i = 0; i <sm.getTitles().size();i++){
                combinedResults.add(new CombinedResults(
                        sm.getTitles().get(i),
                        sm.getFinalPosterCall().get(i),
                        sm.getOverview().get(i),
                        0
                ));
            }
        }
        if (sm.getTitlesSeries() != null && sm.getTitlesSeries().size()>0){
            for(int i = 0; i<sm.getTitlesSeries().size();i++){
                combinedResults.add(new CombinedResults(
                        sm.getTitlesSeries().get(i),
                        sm.getFinalPosterCallSeries().get(i),
                        sm.getOverviewSeries().get(i),
                        1
                ));
            }
        }
        if (combinedResults != null && combinedResults.size()>1){
            CombinedResults firstEntry = combinedResults.get(0);
            CombinedResults secondEntry = combinedResults.get(1);
            combinedResults.remove(0);
            combinedResults.remove(1);

            Collections.sort(combinedResults, new Comparator<CombinedResults>() {
                @Override
                public int compare(CombinedResults o1, CombinedResults o2) {
                    return o1.getTitle().compareTo(o2.getTitle());
                }
            });
            combinedResults.add(0,firstEntry);
            combinedResults.add(1,secondEntry);

            return combinedResults;
        }else{
            return combinedResults;
        }



    }

    @Override
    public int getItemCount() {
        return combinedResults.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView heading,content;
        ImageView poster;

        public ViewHolder(View itemView) {
            super(itemView);
            heading = itemView.findViewById(R.id.title_text_card_layout);
            content = itemView.findViewById(R.id.description_text_card_layout);
            poster = itemView.findViewById(R.id.image_resource_card_layout);
        }
    }
}
