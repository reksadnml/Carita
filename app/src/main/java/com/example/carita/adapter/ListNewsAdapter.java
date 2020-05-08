package com.example.carita.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.carita.NewsDetail;
import com.example.carita.R;
import com.example.carita.dataclass.News;

import java.util.ArrayList;

public class ListNewsAdapter extends RecyclerView.Adapter<ListNewsAdapter.ListViewHolder> {
    ArrayList mData = new ArrayList<News>();

    void setData(ArrayList<News> items) {
        mData.clear();
        mData.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListNewsAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_berita, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListNewsAdapter.ListViewHolder holder, int position) {
        final News data = (News) mData.get(position);
        holder.viewTitle.setText(data.getTitle());
        holder.viewContent.setText(data.getContent());
        Glide.with(holder.itemView.getContext())
                .load(data.getPhoto())
                .apply(new RequestOptions().override(70, 80))
                .into(holder.viewPhoto);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), NewsDetail.class);
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
            viewTitle.findViewById(R.id.news_title);
            viewContent.findViewById(R.id.news_content);
            viewPhoto.findViewById(R.id.news_photo);
        }
    }
}
