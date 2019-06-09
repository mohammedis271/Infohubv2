package com.example.muaazjoosuf.infohubv2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsArticleAdapter extends RecyclerView.Adapter<NewsArticleAdapter.NewsArticleViewHolder>{

    ArrayList<NewsArticle> arrayList;
    Context c;

    public NewsArticleAdapter(Context c,ArrayList<NewsArticle> arrayList) {
        this.arrayList = arrayList;
        this.c = c;
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    @NonNull
    @Override
    public NewsArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.card_layout,parent,false);
        NewsArticleViewHolder newsArticleViewHolder = new NewsArticleViewHolder(view);
        view.setOnClickListener(mOnClickListener);
        return newsArticleViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsArticleViewHolder holder, int position) {
        NewsArticle newsArticle = arrayList.get(position);
        holder.titleTextView.setText(newsArticle.getTitle());
        holder.descriptionTextView.setText(newsArticle.getDescription());
        holder.imageViewArticle.setImageBitmap(newsArticle.getImage());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class NewsArticleViewHolder extends RecyclerView.ViewHolder{

        TextView titleTextView,descriptionTextView;
        ImageView imageViewArticle;

        public NewsArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title_text_card_layout);
            descriptionTextView = itemView.findViewById(R.id.description_text_card_layout);
            imageViewArticle = itemView.findViewById(R.id.image_resource_card_layout);
        }
    }
}
