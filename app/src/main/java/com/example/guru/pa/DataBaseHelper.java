package com.example.guru.pa;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Haoyu on 2016/7/20.
 */
public class DataBaseHelper extends SQLiteOpenHelper{

    private static int VERSION = 1;
    private static String SQL_NORMAL_ONE = "CREATE TABLE if not exists schedule(" +
            "id integer primary key autoincrement," +
            "year integer, month integer, day integer," +
            "hour integer, minute integer, second integer," +
            "assignment varchar(200)" +
            ")";
    private static String SQL_TAG_ONE = "CREATE TABLE if not exists tagSchedule(" +
            "tagId integer primary key autoincrement" +
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
