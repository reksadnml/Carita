package com.example.carita.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carita.R;
import com.example.carita.adapter.NotificationAdapter;
import com.example.carita.dataclass.Notif;
import com.example.carita.viewmodel.NotificationsViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment implements View.OnClickListener{

    private NotificationsViewModel notificationsViewModel;
    private RecyclerView rvNotification;
    private NotificationAdapter listNotificationAdapter;
    private View root;
    private Button clearNotifications;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        root = inflater.inflate(R.layout.fragment_notifications, container, false);
        clearNotifications = root.findViewById(R.id.clear_notification);
        clearNotifications.setOnClickListener(this);

        setAdapter();
        setRecyclerView();

        return root;
    }

    private void setAdapter() {
        listNotificationAdapter = new NotificationAdapter();
        listNotificationAdapter.notifyDataSetChanged();
    }

    private void setRecyclerView() {
        rvNotification = root.findViewById(R.id.rv_notification_list);
        rvNotification.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvNotification.setAdapter(listNotificationAdapter);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        notificationsViewModel.setNotifList(getActivity());
        notificationsViewModel.getNews().observe(getActivity(), notifs -> {
            if (notifs != null && notifs.size() != 0) {
                listNotificationAdapter.setData(notifs);
            } else {
                clearNotifications.setVisibility(View.GONE);
                Toast.makeText(getActivity(), getResources().getString(R.string.no_notification), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clear_notification:
                Toast.makeText(getActivity(), getResources().getString(R.string.clear_notification_history), Toast.LENGTH_SHORT).show();
                ArrayList<Notif> newList = new ArrayList<>();

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("notification", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();

                String jsonPut = gson.toJson(newList);
                editor.putString("notification list", jsonPut);
                editor.apply();

                listNotificationAdapter.setData(newList);
                clearNotifications.setVisibility(View.GONE);
        }
    }
}
