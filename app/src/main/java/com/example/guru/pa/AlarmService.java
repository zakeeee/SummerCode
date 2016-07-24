package com.example.guru.pa;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

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
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        alarmClock = new AlarmClock(trigger, interval, alarmId, alarmContent);

        alarmClock.setAlarm(this);
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

}
