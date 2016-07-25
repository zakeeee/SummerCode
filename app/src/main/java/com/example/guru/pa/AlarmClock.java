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

    private String content = "新的提醒\n";

    public AlarmClock() {
        //必须要有,没有就报错
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();
        content += intent.getStringExtra("pa.setAlarm.alarmClock.message");

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


}
