package com.example.guru.pa;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Haoyu on 2016/7/20.
 */
public class DataBaseHelper extends SQLiteOpenHelper{

    public static final String TABLE_NORMAL = "schedule";
    public static final String TABLE_TAG = "tagSchedule";

    private static int VERSION = 1;
    private static String SQL_NORMAL_ONE = "CREATE TABLE if not exists " + TABLE_NORMAL +
            "(" +
            "scheduleId integer primary key autoincrement," +
            "date text," +
            "time text," +
            "content text" +
            ")";
    private static String SQL_TAG_ONE = "CREATE TABLE if not exists " + TABLE_TAG +
            "(" +
            "tagId integer primary key autoincrement," +
            "scheduleId integer," +
            "remindId integer," +
            "year integer, month integer, day integer" +
            ")";

    private static String SQL_NORMAL_TWO = "DROP TABLE if exists schedule";
    private static String SQL_TAG_TWO = "DROP TABLE if exists tagSchedule";

    public DataBaseHelper(Context context, String dataBaseName, SQLiteDatabase.CursorFactory factory, int version){
        super(context, dataBaseName, factory,version);
    }
    public DataBaseHelper(Context context, String dataBaseName, SQLiteDatabase.CursorFactory factory){
        this(context, dataBaseName, factory, VERSION);
    }
    public DataBaseHelper(Context context, String dataBaseName) {
        this(context, dataBaseName, null);
    }


    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_NORMAL_ONE);
        db.execSQL(SQL_TAG_ONE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(SQL_NORMAL_TWO);
        db.execSQL(SQL_TAG_TWO);
        onCreate(db);
    }
}
