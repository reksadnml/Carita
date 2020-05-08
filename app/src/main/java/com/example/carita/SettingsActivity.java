package com.example.carita;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.carita.receiver.ReminderReceiver;

import java.util.Calendar;

import static com.example.carita.receiver.ReminderReceiver.EXTRA_TYPE;
import static com.example.carita.receiver.ReminderReceiver.REMINDER_ID;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    static final String PREF_SWITCH_DAILY_REMINDER = "daily reminder";
    static final String PREF_SWITCH_DAILY_NEW = "new news";

    private AlarmManager dailyAlarmManager;
    private AlarmManager newsAlarmManager;
    private Intent dailyIntent;
    private Intent newsIntent;
    private PendingIntent dailyPendingIntent;
    private PendingIntent newsPendingIntent;
    private Calendar calendar = Calendar.getInstance();

    private View layoutLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        dailyAlarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        newsAlarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        // daily reminder
        dailyIntent = new Intent(this, ReminderReceiver.class);
        dailyIntent.putExtra(EXTRA_TYPE, "daily reminder");
        dailyPendingIntent = PendingIntent.getBroadcast(
                this, REMINDER_ID, dailyIntent, 0);


        // new news
        newsIntent = new Intent(this, ReminderReceiver.class);
        newsIntent.putExtra(EXTRA_TYPE, "new news");
        newsPendingIntent = PendingIntent.getBroadcast(
                this, REMINDER_ID, newsIntent, 0);


        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        layoutLanguage.findViewById(R.id.layout_language);
        layoutLanguage.setOnClickListener(this);

        setSwitch();

    }

    private void setSwitch() {
        SharedPreferences sharedPreferences = getSharedPreferences("switch settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


    }

    @Override
    public void onClick(View view) {

    }
}
