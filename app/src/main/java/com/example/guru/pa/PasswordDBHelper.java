package com.example.guru.pa;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Crystal on 2016/7/22.
 */
public class PasswordDBHelper extends SQLiteOpenHelper{
    public static final String TABLE = "passwordmessage";

    private static int VERSION = 1;
    private static String SQL_ONE = "CREATE TABLE if not exists " + TABLE +
            "(" +
            "id integer primary key autoincrement," +
            "purpose text," +
            "username text," +
            "password text" +
            "extra text" +
            ")";

    private static String SQL_TWO = "DROP TABLE if exists schedule";

    public PasswordDBHelper(Context context, String dataBaseName, SQLiteDatabase.CursorFactory factory, int version){
        super(context, dataBaseName, factory,version);
    }
    public PasswordDBHelper(Context context, String dataBaseName, SQLiteDatabase.CursorFactory factory){
        this(context, dataBaseName, factory, VERSION);
    }
    public PasswordDBHelper(Context context, String dataBaseName) {
        this(context, dataBaseName, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_ONE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(SQL_TWO);
        onCreate(db);
    }
}
