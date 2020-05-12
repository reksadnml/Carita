package com.example.carita.ui.home;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.carita.BuildConfig;
import com.example.carita.dataclass.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsViewModel extends ViewModel {

    private final String NEWS_API_KEY = "9d8f14bcf23749a3acf0654339332edd";

    private MutableLiveData<ArrayList<News>> newsList = new MutableLiveData<>();

    public void setNews(final Context context, String lang, String category) {
        final ArrayList<News> list = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(context);

        String url;

        if (category == null || category == "")
            url = "http://newsapi.org/v2/top-headlines?country=us&apiKey=" + NEWS_API_KEY;
        else url = "http://newsapi.org/v2/top-headlines?country=us&category=" + category + "&apiKey=" + NEWS_API_KEY;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("articles");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject news = jsonArray.getJSONObject(i);
                        String title = news.getString("title");
                        String content = news.getString("content");
                        String link = news.getString("url");
                        String urlPhoto = news.getString("urlToImage");
                        News itemNews = new News(title, content, link, urlPhoto);
                        list.add(itemNews);
                        Log.d("check", title);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                newsList.postValue(list);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Maaf tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
                Log.d("Error Response", error.toString());
            }
        });
        queue.add(request);
    }

    public void setSearchNews(final Context context, String query) {
        final ArrayList<News> list = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject news = jsonArray.getJSONObject(i);

                        News itemRegional = new News();
                        list.add(itemRegional);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                newsList.postValue(list);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Maaf tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
                Log.d("Error Response", error.toString());
            }
        });
        queue.add(request);
    }

    public MutableLiveData<ArrayList<News>> getNews() {
        return newsList;
    }
}