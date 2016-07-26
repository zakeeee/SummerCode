package com.example.guru.pa;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
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
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Random;

/**
 * Created by Haoyu on 2016/7/24.
 */
public class AlarmClock extends BroadcastReceiver {

    private String mContent;
    private String mDate;

    public AlarmClock() {
        //必须要有,没有就报错
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();
        mContent = intent.getStringExtra("pa.setAlarm.alarmClock.message");
        mDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());

        Toast.makeText(context, "您有新的提醒！", Toast.LENGTH_LONG).show();

        Vibrator  vv =(Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vv.vibrate(1500);

        NotificationManager notificationManager
                = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_message)
                        .setContentTitle(mDate + " 日程安排")
                        .setContentText(mContent)
                        .setSound(soundUri);
        notificationManager.notify(0, mBuilder.build());

        wl.release();
    }


}
