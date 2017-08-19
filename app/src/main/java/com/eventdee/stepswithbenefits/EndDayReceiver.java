package com.eventdee.stepswithbenefits;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class EndDayReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() != null) {
            switch (intent.getAction()) {
                case "android.intent.action.BOOT_COMPLETED":
                    setAlarm(context);
                    break;
                case "android.intent.action.ACTION_SHUTDOWN":

                    break;
                case "android.intent.action.QUICKBOOT_POWEROFF":

                    break;
                default:
//                    Toast.makeText(context, "Action: " + intent.getAction(), Toast.LENGTH_SHORT).show();
                    System.out.println("ryantkm: " + intent.getAction());
            }
        } else {
            System.out.println("ryantkm: " + intent.getAction());

            SharedPreferences sharedPref = context.getSharedPreferences("dailySteps", Context.MODE_PRIVATE);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference stepsDB = database.getReference("steps");
            stepsDB.setValue(sharedPref.getString("steps", null));
        }
    }

    public void setAlarm(Context context) {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, EndDayReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        // Set the alarm to start at 12am
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);
    }
}
