package com.example.guru.pa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by Haoyu on 2016/7/20.
 */
public class DataBaseOperator {
    public static final String DB_NAME_SCHEDULE = "mySchedule.db";
    private DataBaseHelper mDBOpenHelper = null;
    public DataBaseOperator(Context context){
        mDBOpenHelper = new DataBaseHelper(context, DB_NAME_SCHEDULE);
    }
    /**
     * @param schedule
     * @return
     */
    public int saveSchedule(Schedule schedule){
        SQLiteDatabase db = mDBOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", schedule.getDate());
        values.put("time", schedule.getTime());
        values.put("content", schedule.getContent());
        db.beginTransaction();
        int scheduleId = -1;
        try {
            String sql = "select max(scheduleId) from" + " " + DataBaseHelper.TABLE_NORMAL;
            db.insert(DataBaseHelper.TABLE_NORMAL, null, values);
            Cursor cursor = db.rawQuery(sql, null);
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

    /**
     * @param scheduleId
     * @return
     */
    public Schedule getScheduleById(int scheduleId){
        SQLiteDatabase db = mDBOpenHelper.getWritableDatabase();
        String[] columns = {"scheduleId", "date", "time", "content"};
        String selections = "scheduleId=?";
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

    /**
     * @return
     */
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
        if (arrayList != null && arrayList.size() > 0){
            return arrayList;
        }
        return null;
    }

    /**
     * @param scheduleId
     */
    public void deleteScheduleById(int scheduleId){
        SQLiteDatabase db = mDBOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            String whereClause = "scheduleId=?";
            String[] whereArgs = {String.valueOf(scheduleId)};
            db.delete(DataBaseHelper.TABLE_NORMAL, whereClause, whereArgs);
            db.delete(DataBaseHelper.TABLE_TAG, whereClause, whereArgs);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /**
     *
     * @param scheduleId
     */
    public void deleteTagScheduleById(int scheduleId){
        SQLiteDatabase db = mDBOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            String whereClause = "scheduleId=?";
            String[] whereArgs = {String.valueOf(scheduleId)};
            db.delete(DataBaseHelper.TABLE_TAG, whereClause, whereArgs);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * @param newSchedule
     */
    public void updateSchedule(Schedule newSchedule){
        SQLiteDatabase db = mDBOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", newSchedule.getDate());
        values.put("time", newSchedule.getTime());
        values.put("content", newSchedule.getContent());
        values.put("scheduleId", newSchedule.getScheduleId());
        db.update(DataBaseHelper.TABLE_NORMAL, values,
                "scheduleId=?", new String[]{String.valueOf(newSchedule.getScheduleId())});
    }

    /**
     *
     * @param tagSchedule
     */
    public void updateTagSchedule(TagSchedule tagSchedule){
        SQLiteDatabase db = mDBOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("scheduleId",tagSchedule.getScheduleId());
        values.put("remindId",tagSchedule.getRemindId());
        values.put("year",tagSchedule.getYear());
        values.put("month",tagSchedule.getMonth());
        values.put("day",tagSchedule.getDay());
        db.update(DataBaseHelper.TABLE_TAG, values,
                "scheduleId=?", new String[]{String.valueOf(tagSchedule.getScheduleId())});
    }

    /**
     * @param tagSchedule
     * @return
     */
    public int saveTagSchedule(TagSchedule tagSchedule){
        SQLiteDatabase db = mDBOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("scheduleId",tagSchedule.getScheduleId());
        values.put("remindId",tagSchedule.getRemindId());
        values.put("year",tagSchedule.getYear());
        values.put("month",tagSchedule.getMonth());
        values.put("day",tagSchedule.getDay());
        db.beginTransaction();
        int tagId = -1;
        try {
            String sql = "select max(tagId) from" + " " + DataBaseHelper.TABLE_TAG;
            db.insert(DataBaseHelper.TABLE_TAG, null, values);
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()){
                tagId = (int)cursor.getLong(0);
            }
            cursor.close();
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
        return tagId;
    }

    /**
     * @param tagScheduleArrayList
     */
    public void saveTagScheduleList(ArrayList<TagSchedule> tagScheduleArrayList){
        SQLiteDatabase db = mDBOpenHelper.getWritableDatabase();
        TagSchedule tagSchedule = new TagSchedule();
        db.beginTransaction();
        try {
            for (int i = 0; i < tagScheduleArrayList.size(); ++ i){
                tagSchedule = tagScheduleArrayList.get(i);
                ContentValues values = new ContentValues();
                values.put("scheduleId",tagSchedule.getScheduleId());
                values.put("remindId",tagSchedule.getRemindId());
                values.put("year",tagSchedule.getYear());
                values.put("month",tagSchedule.getMonth());
                values.put("day",tagSchedule.getDay());
                db.insert(DataBaseHelper.TABLE_TAG, null, values);
            }

            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
    }

    /**
     * 查询当前月份的标记日程
     * @param currentYear
     * @param currentMonth
     * @return
     */
    public ArrayList<TagSchedule> getTagScheduleByMY(int currentYear, int currentMonth){
        SQLiteDatabase db = mDBOpenHelper.getWritableDatabase();
        ArrayList<TagSchedule> retList = new ArrayList<TagSchedule>();
        String[] columns = {"tagId", "scheduleId", "remindId", "year", "month", "day"};
        String selections = "year=? and month=?";
        String[] selectionArgs = {String.valueOf(currentYear), String.valueOf(currentMonth)};
        Cursor cursor = db.query(DataBaseHelper.TABLE_TAG, columns, selections,
                selectionArgs, null, null, null);
        while (cursor.moveToNext()){
            int gTagId = cursor.getInt(cursor.getColumnIndex("tagId"));
            int gScheduleId = cursor.getInt(cursor.getColumnIndex("scheduleId"));
            int gRemindId = cursor.getInt(cursor.getColumnIndex("remindId"));
            int gYear = cursor.getInt(cursor.getColumnIndex("year"));
            int gMonth = cursor.getInt(cursor.getColumnIndex("month"));
            int gDay = cursor.getInt(cursor.getColumnIndex("day"));
            retList.add(new TagSchedule(gYear, gMonth, gDay, gRemindId, gTagId, gScheduleId));
        }
        cursor.close();
        if (retList != null && retList.size() > 0) {
            return retList;
        }
        return null;
    }

    /**
     *
     * @param scheduleId
     * @return
     */
    public TagSchedule getTagScheduleById(int scheduleId){
        SQLiteDatabase db = mDBOpenHelper.getWritableDatabase();
        String[] columns = {"tagId", "scheduleId", "remindId", "year", "month", "day"};
        String selections = "scheduleId=?";
        String[] selectionArgs = {String.valueOf(scheduleId)};
        Cursor cursor = db.query(DataBaseHelper.TABLE_TAG, columns,
                selections ,selectionArgs, null, null, null);
        if (cursor.moveToFirst()){
            int gTagId = cursor.getInt(cursor.getColumnIndex("tagId"));
            int gScheduleId = cursor.getInt(cursor.getColumnIndex("scheduleId"));
            int gRemindId = cursor.getInt(cursor.getColumnIndex("remindId"));
            int gYear = cursor.getInt(cursor.getColumnIndex("year"));
            int gMonth = cursor.getInt(cursor.getColumnIndex("month"));
            int gDay = cursor.getInt(cursor.getColumnIndex("day"));
            cursor.close();
            return new TagSchedule(gYear, gMonth, gDay, gRemindId, gTagId, gScheduleId);
        }
        cursor.close();
        return null;
    }
/*
    public void deleteAll(){
        mDBOpenHelper.onUpgrade(mDBOpenHelper.getWritableDatabase(),1, 1);
    }
*/

    public void closeDB() {
        if (mDBOpenHelper != null){
            mDBOpenHelper.close();
        }
    }
}
