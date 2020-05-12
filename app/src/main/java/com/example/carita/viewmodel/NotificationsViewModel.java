package com.example.carita.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.carita.dataclass.News;
import com.example.carita.dataclass.Notif;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class NotificationsViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Notif>> notifList = new MutableLiveData<>();

    public void setNotifList(final Context context) {
        final ArrayList<Notif> list;

        SharedPreferences sharedPreferences = context.getSharedPreferences("notification", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        String jsonGet = sharedPreferences.getString("notification list", null);
        if (jsonGet != null) {
            Type type = new TypeToken<ArrayList<Notif>>() {}.getType();
            list = gson.fromJson(jsonGet, type);
        } else {
            list = new ArrayList<>();
        }
        notifList.postValue(list);
    }

    public MutableLiveData<ArrayList<Notif>> getNews() {
        return notifList;
    }
}