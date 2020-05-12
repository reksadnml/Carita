package com.example.carita.receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.carita.BaseActivity;
import com.example.carita.R;

import org.json.JSONObject;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String EXTRA_TYPE = "type";
    public static final int REMINDER_ID = 101;

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = "Don't forget to check Carita today!";
        String type = intent.getStringExtra(EXTRA_TYPE);
        String title = "See the News";

        if (type == "daily reminder") {
            // this notification is for daily reminder
            showNotification(context, title, message, REMINDER_ID);
        } else if (type == "new news") {
            // this notification is for new news, it will show as a list
            getNewsData(context);
        }
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void getNewsData(Context context) {
        String url = "";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    private void showNotification(Context context, String title, String message, int ReminderId) {
        String CHANNEL_ID = "Channel_1";
        String CHANNEL_NAME = "Daily Reminder Channel";

        Intent contentIntent = new Intent(context, BaseActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(context, REMINDER_ID,
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
