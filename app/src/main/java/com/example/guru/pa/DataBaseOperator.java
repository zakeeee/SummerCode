package com.example.guru.pa;

import android.content.Context;

/**
 * Created by Haoyu on 2016/7/20.
 */
public class DataBaseOperator {
    private DataBaseHelper mDataBaseHelper = null;
    private static String DBNAME = "schedule.db";
    public DataBaseOperator(Context context){
        mDataBaseHelper = new DataBaseHelper(context, DBNAME);
    }


}
