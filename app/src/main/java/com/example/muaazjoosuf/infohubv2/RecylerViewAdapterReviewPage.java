package com.example.muaazjoosuf.infohubv2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecylerViewAdapterReviewPage extends RecyclerView.Adapter<RecylerViewAdapterReviewPage.ViewHolder> {

    private Context context;
    private SelectedMovieCallHandler sm;
    private String query;

    public RecylerViewAdapterReviewPage(Context context,String query,String section) {
        this.query = query;
        this.context = context;

        sm = new SelectedMovieCallHandler(query,section);
        sm.sortJSONDataIntoArrays();
        sm.fetchMovieReviews();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.author.setText(new StringBuilder().append("Author: ").append(sm.getReviewAuthor().get(position)).toString());
        holder.content.setText(new StringBuilder().append("Content: ").append(sm.getReviewContent().get(position)).toString());
    }

    @Override
    public int getItemCount() {
        return sm.getReviewAuthor().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView author,content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.review_card_author);
            content = itemView.findViewById(R.id.review_card_content);
        }
    }
}
