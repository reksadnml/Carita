package com.example.carita.receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.carita.BaseActivity;
import com.example.carita.R;
import com.example.carita.dataclass.News;
import com.example.carita.dataclass.Notif;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

import static com.example.carita.BuildConfig.NEWS_API_KEY;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String EXTRA_TYPE = "type";
    public static final int DAILY_NEWS_ID = 101;
    String messageForNotification;
    String title;
    int MAX_CONTENT_LENGTH = 80;

    @Override
    public void onReceive(Context context, Intent intent) {
        title = context.getResources().getString(R.string.notification_title);
        setNewsNotification(context);
    }

    public void setNewsNotification(final Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);

        String lang = Locale.getDefault().toLanguageTag();
        if (lang == "in-ID") lang = "id";
        else lang = "us";

        String url = "http://newsapi.org/v2/top-headlines?country=" + lang + "&apiKey=" + NEWS_API_KEY;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("articles");

                    JSONObject news = jsonArray.getJSONObject(0);
                    messageForNotification = news.getString("title");

                    // set max content length
                    if (messageForNotification.length() > MAX_CONTENT_LENGTH) {
                        messageForNotification = messageForNotification.substring(0, MAX_CONTENT_LENGTH) + "...";
                    }

                    showNotification(context, title, messageForNotification, DAILY_NEWS_ID);

                    SharedPreferences sharedPreferences = context.getSharedPreferences("notification", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    Notif notif = new Notif(title, messageForNotification);
                    ArrayList<Notif> listNotif;
                    Gson gson = new Gson();

                    String jsonGet = sharedPreferences.getString("notification list", null);
                    if (jsonGet == null) {
                        listNotif = new ArrayList<>();
                    } else {
                        Type type = new TypeToken<ArrayList<Notif>>() {}.getType();
                        listNotif = gson.fromJson(jsonGet, type);
                    }

                    listNotif.add(notif);
                    String jsonPut = gson.toJson(listNotif);
                    editor.putString("notification list", jsonPut);
                    editor.apply();

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

    private void showNotification(Context context, String title, String message, int ReminderId) {
        String CHANNEL_ID = "Channel_1";
        String CHANNEL_NAME = "Daily Reminder Channel";

        Intent contentIntent = new Intent(context, BaseActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(context, DAILY_NEWS_ID,
                contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notif)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setContentIntent(contentPendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(alarmSound);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);

            builder.setChannelId(CHANNEL_ID);
            notificationManagerCompat.createNotificationChannel(channel);
        }

        Notification notification = builder.build();
        notificationManagerCompat.notify(ReminderId, notification);
    }
}
