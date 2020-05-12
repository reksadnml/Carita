package com.example.carita.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.carita.R;
import com.example.carita.receiver.NotificationReceiver;

import java.util.Calendar;

import static com.example.carita.receiver.NotificationReceiver.DAILY_NEWS_ID;
import static com.example.carita.receiver.NotificationReceiver.EXTRA_TYPE;

public class SettingsFragment extends Fragment implements View.OnClickListener{

    static final String PREF_SWITCH_DAILY_NEWS = "new news";

    private AlarmManager dailyNewsManager;
    private Intent dailyNewsIntent;
    private PendingIntent dailyNewsPendingIntent;
    private Calendar calendar = Calendar.getInstance();

    private View layoutLanguage;
    private View root;

    private Switch dailyNewsSwitch;

    // perulangan notif
    private long sixHour = 21600L;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_settings, container, false);

        dailyNewsManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        // new news
        dailyNewsIntent = new Intent(getActivity(), NotificationReceiver.class);
        dailyNewsIntent.putExtra(EXTRA_TYPE, "new news");
        dailyNewsPendingIntent = PendingIntent.getBroadcast(
                getActivity(), DAILY_NEWS_ID, dailyNewsIntent, 0);

        layoutLanguage = root.findViewById(R.id.layout_language);
        layoutLanguage.setOnClickListener(this);

        setSwitch();
        return root;
    }

    private void setSwitch() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        dailyNewsSwitch = root.findViewById(R.id.daily_news_notification_switch);
        if (sharedPreferences.getBoolean(PREF_SWITCH_DAILY_NEWS, true))
            dailyNewsSwitch.setChecked(true);
        else dailyNewsSwitch.setChecked(false);

        dailyNewsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean(PREF_SWITCH_DAILY_NEWS, true);
                    setRepeatingAlarm();
                    Toast.makeText(compoundButton.getContext(), getResources().getString(R.string.daily_notification_on),
                            Toast.LENGTH_SHORT).show();
                } else {
                    editor.putBoolean(PREF_SWITCH_DAILY_NEWS, false);
                    dailyNewsManager.cancel(dailyNewsPendingIntent);
                    Toast.makeText(compoundButton.getContext(), getString(R.string.daily_notification_off),
                            Toast.LENGTH_SHORT).show();
                }
                editor.apply();
            }

        });
    }

    private void setRepeatingAlarm() {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.HOUR_OF_DAY)>=9 && calendar.get(Calendar.MINUTE)>0) calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // perulangan notif
        dailyNewsManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                sixHour*1000,
                dailyNewsPendingIntent
        );
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_language:
                startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
                break;
        }
    }
}
