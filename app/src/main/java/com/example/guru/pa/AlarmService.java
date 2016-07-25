package com.example.guru.pa;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Random;

public class AlarmService extends Service {
    private AlarmClock alarmClock;
    private long trigger;
    private long interval;
    private int  alarmId;
    private String alarmContent;
    private final IBinder mBinder = new LocalBinder();


    public class LocalBinder extends Binder {
        public AlarmService getService() {
            // Return this instance of LocalService so clients can call public methods
            return AlarmService.this;
        }
    }

    @Override
    public void onCreate() {
        alarmClock = new AlarmClock();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        setAlarm(this);
        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void setTimeAndContent(long trigger, long interval, int id, String content){
        this.trigger = trigger;
        this.interval = interval;
        this.alarmId = id;
        this.alarmContent = content;
    }

    public void setAlarm(Context context)
    {

        AlarmManager alarmManager =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmClock.class);
        intent.putExtra("pa.setAlarm.alarmClock.message", alarmContent);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (interval < 0) {
            alarmManager.set(AlarmManager. ELAPSED_REALTIME_WAKEUP, trigger, pendingIntent);
        }
        else {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, trigger, interval, pendingIntent);
        }


    }

    public void cancelAlarm(Context context)
    {
        Intent intent = new Intent(context, AlarmClock.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

}
