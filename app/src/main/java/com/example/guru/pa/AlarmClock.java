package com.example.guru.pa;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by Haoyu on 2016/7/24.
 */
public class AlarmClock extends BroadcastReceiver {

    private long triggerAtMillis;
    private long intervalMillis;
    private int alarmId;
    private String content = "新的提醒：\n";

    public AlarmClock() {
        //必须要有,没有就报错
    }

    public AlarmClock(long triggerAtMillis, long intervalMillis, int alarmId, String content) {
        this.triggerAtMillis = triggerAtMillis;
        this.intervalMillis = intervalMillis;
        this.alarmId = alarmId;
        this.content += content;
    }
    @Override
    public void onReceive(Context context, Intent intent)
    {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();


        Toast.makeText(context, content, Toast.LENGTH_LONG).show();

        Vibrator  vv =(Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vv.vibrate(3000);

        //Define Notification Manager
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //Define sound URI
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //Create and set the sound
        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder)
                new NotificationCompat.Builder(context).setSound(soundUri);
        //Display notification
        notificationManager.notify(0, mBuilder.build());

        wl.release();
    }

    public void setAlarm(Context context)
    {

        AlarmManager alarmManager =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmClock.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, triggerAtMillis, intervalMillis, pendingIntent);
    }

    public void cancelAlarm(Context context)
    {
        Intent intent = new Intent(context, AlarmClock.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
