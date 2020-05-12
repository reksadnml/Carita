package com.example.carita.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carita.R;
import com.example.carita.dataclass.News;
import com.example.carita.dataclass.Notif;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ListViewHolder> {
    private ArrayList mData = new ArrayList<Notif>();

    public void setData(ArrayList<Notif> items) {
        mData.clear();
        mData.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_notifs, parent, false);
        return new NotificationAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ListViewHolder holder, int position) {
        final Notif data = (Notif) mData.get(position);
        holder.viewTitle.setText(data.getTitle());
        holder.viewContent.setText(data.getContent());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder{
        TextView viewTitle;
        TextView viewContent;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            viewTitle = itemView.findViewById(R.id.notification_title);
            viewContent = itemView.findViewById(R.id.notification_content);
        }
    }
}
