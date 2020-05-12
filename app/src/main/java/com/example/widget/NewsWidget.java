package com.example.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.carita.R;
import com.example.carita.dataclass.News;
import com.example.carita.viewmodel.NewsViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static com.example.carita.BuildConfig.NEWS_API_KEY;

/**
 * Implementation of App Widget functionality.
 */
public class NewsWidget extends AppWidgetProvider {

    private static final String LIST_1 = "list 1";
    private static final String LIST_2 = "list 2";
    private static final String LIST_3 = "list 3";
    private static long seJam = 3600L;
    private static NewsViewModel newsViewModel;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.news_widget);

        String lang = Locale.getDefault().toLanguageTag();
        if (lang == "in-ID") lang = "id";
        else lang = "us";
        setWidgetNews(context, lang);

        SharedPreferences sharedPreferences = context.getSharedPreferences("widget data", Context.MODE_PRIVATE);
        views.setTextViewText(R.id.notification_title_1, sharedPreferences.getString(LIST_1, "O"));
        views.setTextViewText(R.id.notification_title_2, sharedPreferences.getString(LIST_2, "-"));
        views.setTextViewText(R.id.notification_title_3, sharedPreferences.getString(LIST_3, "-"));

        Intent intentUpdate = new Intent(context, NewsWidget.class);
        intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        int[] idArray = new int[]{appWidgetId};
        intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray);
        PendingIntent pendingUpdate = PendingIntent.getBroadcast(
                context, appWidgetId, intentUpdate,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
                seJam*1000, pendingUpdate);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void setWidgetNews(final Context context, String lang) {
        RequestQueue queue = Volley.newRequestQueue(context);

        String url = "http://newsapi.org/v2/top-headlines?country=" + lang + "&apiKey=" + NEWS_API_KEY;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("articles");
                    SharedPreferences sharedPreferences = context.getSharedPreferences("widget data", Context.MODE_PRIVATE);;
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    for (int i = 0; i < 3; i++) {
                        JSONObject news = jsonArray.getJSONObject(i);
                        String title = news.getString("title");
                        String content = news.getString("content");
                        String link = news.getString("url");
                        String urlPhoto = news.getString("urlToImage");

                        switch (i) {
                            case 0:
                                editor.putString(LIST_1, news.getString("title"));
                                break;
                            case 1:
                                editor.putString(LIST_2, news.getString("title"));
                                break;
                            case 2:
                                editor.putString(LIST_3, news.getString("title"));
                                break;
                        }
                        editor.apply();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, context.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                Log.d("Error Response", error.toString());
            }
        });
        queue.add(request);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

