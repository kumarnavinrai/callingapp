package com.example.dhass;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.Toast;

import com.example.dhass.MyBroadcastReciever;

import java.util.Calendar;
import java.util.Date;

public class AlarmScheduler {
    private static final int ALARM_REQUEST_CODE = 123; // Replace with your desired request code

    public static void scheduleRepeatingAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, MyBroadcastReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_MUTABLE);
//
//        // Set the alarm to start in 30 seconds and repeat every 30 seconds
//        long initialDelayMillis = SystemClock.elapsedRealtime() + 30 * 1000; // 30 seconds
//        long repeatIntervalMillis = 30 * 1000; // 30 seconds
//
//        alarmManager.setRepeating(
//                AlarmManager.ELAPSED_REALTIME,
//                initialDelayMillis,
//                repeatIntervalMillis,
//                pendingIntent
//        );

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 30, pendingIntent);

    }

    public static void cancelRepeatingAlarm(Context context) {
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Intent alarmIntent = new Intent(context, MyBroadcastReciever.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent,  PendingIntent.FLAG_MUTABLE);
//
//        // Cancel the repeating alarm
//        alarmManager.cancel(pendingIntent);
//        // You should also cancel the pending intent
//        pendingIntent.cancel();
    }

    public static boolean isRepeatingAlarmSet(Context context, int requestCode) {
        Intent intent = new Intent(context, MyBroadcastReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE);

        return pendingIntent != null;
    }

    public static void scheduleAlarmOnce(Context context) {



//        Date dat  = new Date();//initializes to now
//        Calendar cal_alarm = Calendar.getInstance();
//        Calendar cal_now = Calendar.getInstance();
//        cal_now.setTime(dat);
//        cal_alarm.setTime(dat);
//        cal_alarm.set(Calendar.HOUR_OF_DAY,4);//set the alarm time
//        cal_alarm.set(Calendar.MINUTE, 00);
//        cal_alarm.set(Calendar.SECOND,0);
//        if(cal_alarm.before(cal_now)){//if its in the past increment
//            cal_alarm.add(Calendar.DATE,1);
//        }
         // Set the time at which you want the alarm to go off (in milliseconds since epoch)
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//
//        calendar.set(Calendar.HOUR_OF_DAY, 3); // Replace with your desired hour
//        calendar.set(Calendar.MINUTE, 57);      // Replace with your desired minute
//        calendar.set(Calendar.SECOND, 0);
//        long alarmTime = cal_alarm.getTimeInMillis();

        // Create an intent to trigger the AlarmReceiver class
//        Intent alarmIntent = new Intent(context, MyBroadcastReciever.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ALARM_REQUEST_CODE, alarmIntent, PendingIntent.FLAG_MUTABLE);
//
//        // Get the AlarmManager service
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//
//        // Set the alarm to wake up the device at the specified time
//        // You can use different types of alarms depending on your requirements (RTC, ELAPSED_REALTIME, etc.)
//        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1015, pendingIntent);
//
//        Toast.makeText(context, "Alarm set in 15 seconds",Toast.LENGTH_LONG).show();

    }


}