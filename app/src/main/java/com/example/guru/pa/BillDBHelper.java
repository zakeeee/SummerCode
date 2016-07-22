package com.example.guru.pa;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Haoyu on 2016/7/22.
 */
public class BillDBHelper extends SQLiteOpenHelper {

    public static String TABLE_BILL = "bill";
    private static int VERSION = 1;

    private static String SQL_BILL_ONE = "CREATE TABLE if not exists " + TABLE_BILL +
            "(" +
            "billId integer primary key autoincrement," +
            "income integer," +
            "expend integer," +
            "incomeSource text," +
            "expendDes text,"  +
            "backup text," +
            "year integer," +
            "month integer," +
            "day integer" +
            ")";


   // private static String SQL_BILL_TWO = "DROP TABLE if exists" + " " + TABLE_BILL;

    public BillDBHelper(Context context, String dataBaseName, SQLiteDatabase.CursorFactory factory, int version){
        super(context, dataBaseName, factory,version);
    }
    public BillDBHelper(Context context, String dataBaseName, SQLiteDatabase.CursorFactory factory){
        this(context, dataBaseName, factory, VERSION);
    }
    public BillDBHelper(Context context, String dataBaseName) {
        this(context, dataBaseName, null);
    }


    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_BILL_ONE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
       // db.execSQL(SQL_BILL_TWO);
      //  onCreate(db);
    }
}
