package com.example.carita.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.carita.NewsDetailActivity;
import com.example.carita.R;
import com.example.carita.dataclass.News;

import java.util.ArrayList;

public class ListNewsAdapter extends RecyclerView.Adapter<ListNewsAdapter.ListViewHolder> {
    private ArrayList mData = new ArrayList<News>();
    int MAX_CONTENT_LENGTH = 80;

    public void setData(ArrayList<News> items) {
        mData.clear();
        mData.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListNewsAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_news, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListNewsAdapter.ListViewHolder holder, int position) {
        final News data = (News) mData.get(position);
        holder.viewTitle.setText(data.getTitle());

        String content = data.getContent();

        // set max content length
        if (content.length() > MAX_CONTENT_LENGTH) {
            content = content.substring(0, MAX_CONTENT_LENGTH) + "...";
            holder.viewContent.setText(content);
        } else holder.viewContent.setText(content);

        Glide.with(holder.itemView.getContext())
                .load(data.getUrlPhoto())
                .into(holder.viewPhoto);

        Log.d("check", String.valueOf(data.getTitle()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), NewsDetailActivity.class);
                intent.putExtra("news parcelable", data);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView viewTitle;
        TextView viewContent;
        ImageView viewPhoto;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            viewTitle = itemView.findViewById(R.id.news_title);
            viewContent = itemView.findViewById(R.id.news_content);
            viewPhoto = itemView.findViewById(R.id.news_photo);
        }
    }
}
