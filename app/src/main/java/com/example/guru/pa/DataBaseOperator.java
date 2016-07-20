package com.example.guru.pa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Haoyu on 2016/7/20.
 */
public class DataBaseOperator {
    private DataBaseHelper mDBOpenHelper = null;
    private static String DBNAME = "schedule.db";
    public DataBaseOperator(Context context){
        mDBOpenHelper = new DataBaseHelper(context, DBNAME);
    }
    //保存一个schedule
    public int saveSchedule(Schedule schedule){
        SQLiteDatabase db = mDBOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", schedule.getDate());
        values.put("time", schedule.getTime());
        values.put("content", schedule.getContent());
        values.put("scheduleId", schedule.getScheduleId());
        db.beginTransaction();
        int scheduleId = -1;
        try {
            db.insert(DataBaseHelper.TABLE_NORMAL, null, values);
            Cursor cursor = db.rawQuery("select max(scheduleId) from"
                    + DataBaseHelper.TABLE_NORMAL, null);
            if (cursor.moveToFirst()){
                scheduleId = (int)cursor.getLong(0);
            }
            cursor.close();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return scheduleId;
    }
    //通过Id获得一个schedule
    public Schedule getScheduleById(int scheduleId){
        SQLiteDatabase db = mDBOpenHelper.getWritableDatabase();
        String[] columns = {"scheduleId", "date", "time", "content"};
        String selections = "scheduleId = ?";
        String[] selectionArgs = {String.valueOf(scheduleId)};
        Cursor cursor = db.query(DataBaseHelper.TABLE_NORMAL, columns,
               selections ,selectionArgs, null, null, null);
        if (cursor.moveToFirst()){
            int gottenSId = cursor.getInt(cursor.getColumnIndex("scheduleId"));
            String gottenDate = cursor.getString(cursor.getColumnIndex("date"));
            String gottenTime = cursor.getString(cursor.getColumnIndex("time"));
            String gottenContent = cursor.getString(cursor.getColumnIndex("content"));
            cursor.close();
            return new Schedule(gottenSId, gottenDate, gottenTime, gottenContent);
        }
        cursor.close();
        return null;
    }
    //获得全部的schedule
    public ArrayList<Schedule> getAllSchedule() {
        ArrayList<Schedule> arrayList= new ArrayList<Schedule>();
        SQLiteDatabase db = mDBOpenHelper.getWritableDatabase();
        String[] columns = {"scheduleId", "date", "time", "content"};
        Cursor cursor = db.query(DataBaseHelper.TABLE_NORMAL, columns,
                null, null, null, null, null);
        while (cursor.moveToNext()){
            int gottenSId = cursor.getInt(cursor.getColumnIndex("scheduleId"));
            String gottenDate = cursor.getString(cursor.getColumnIndex("date"));
            String gottenTime = cursor.getString(cursor.getColumnIndex("time"));
            String gottenContent = cursor.getString(cursor.getColumnIndex("content"));
            arrayList.add(
                    new Schedule(gottenSId, gottenDate, gottenTime, gottenContent)
            );
        }
        cursor.close();
        if (arrayList != null && arrayList.size() != 0){
            return arrayList;
        }
        return null;
    }

    /**
     * TO Do:
     * 删除、日程标记、更新等功能
     */

}
