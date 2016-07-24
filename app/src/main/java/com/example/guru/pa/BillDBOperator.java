package com.example.guru.pa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Haoyu on 2016/7/22.
 */
public class BillDBOperator {
    public static final String DB_NAME_BILL = "myBill.db";
    private BillDBHelper mDBOpenHelper = null;
    public BillDBOperator(Context context){
        mDBOpenHelper = new BillDBHelper(context, DB_NAME_BILL);
    }

    /**
     * @param bill
     * @return
     */
    public int saveBill(BillVO bill){
        SQLiteDatabase db = mDBOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("income", bill.getIncome());
        values.put("expend", bill.getExpend());
        values.put("incomeSource", bill.getIncomeSource());
        values.put("expendDes",bill.getExpendDes());
        values.put("backup",bill.getBackup());
        values.put("year",bill.getYear());
        values.put("month",bill.getMonth());
        values.put("day",bill.getDay());

        db.beginTransaction();
        int billId = -1;
        try {
            String sql = "select max(billId) from" + " " + BillDBHelper.TABLE_BILL;
            db.insert(BillDBHelper.TABLE_BILL, null, values);
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()){
                billId = (int)cursor.getLong(0);
            }
            cursor.close();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return billId;
    }



    /**
     * @param billId
     * @return
     */
    public BillVO getBillById(int billId){
        SQLiteDatabase db = mDBOpenHelper.getWritableDatabase();
        String[] columns = {"billId", "income", "expend", "incomeSource", "expendDes",
                "backup", "year", "month", "day"
        };
        String selections = "billId=?";
        String[] selectionArgs = {String.valueOf(billId)};
        Cursor cursor = db.query(BillDBHelper.TABLE_BILL, columns,
                selections ,selectionArgs, null, null, null);
        if (cursor.moveToFirst()){
            int gottenBId = cursor.getInt(cursor.getColumnIndex("billId"));
            int gottenIncome = cursor.getInt(cursor.getColumnIndex("income"));
            int gottenExpend = cursor.getInt(cursor.getColumnIndex("expend"));
            String gottenIS = cursor.getString(cursor.getColumnIndex("incomeSource"));
            String gottenED = cursor.getString(cursor.getColumnIndex("expendDes"));
            String gottenBackup = cursor.getString(cursor.getColumnIndex("backup"));
            int gYear = cursor.getInt(cursor.getColumnIndex("year"));
            int gMonth = cursor.getInt(cursor.getColumnIndex("month"));
            int gDay = cursor.getInt(cursor.getColumnIndex("day"));

            cursor.close();
            return new BillVO(gottenBId, gottenIncome, gottenExpend, gYear, gMonth, gDay,
                    gottenIS, gottenED, gottenBackup
            );
        }
        cursor.close();
        return null;
    }


    /**
     * @return
     */
    public ArrayList<BillVO> getAllBill() {
        ArrayList<BillVO> arrayList= new ArrayList<BillVO>();
        SQLiteDatabase db = mDBOpenHelper.getWritableDatabase();
        String[] columns = {"billId", "income", "expend", "incomeSource", "expendDes",
                "backup", "year", "month", "day"
        };
        Cursor cursor = db.query(BillDBHelper.TABLE_BILL, columns,
                null, null, null, null, null);
        while (cursor.moveToNext()){
            int gottenBId = cursor.getInt(cursor.getColumnIndex("billId"));
            int gottenIncome = cursor.getInt(cursor.getColumnIndex("income"));
            int gottenExpend = cursor.getInt(cursor.getColumnIndex("expend"));
            String gottenIS = cursor.getString(cursor.getColumnIndex("incomeSource"));
            String gottenED = cursor.getString(cursor.getColumnIndex("expendDes"));
            String gottenBackup = cursor.getString(cursor.getColumnIndex("backup"));
            int gYear = cursor.getInt(cursor.getColumnIndex("year"));
            int gMonth = cursor.getInt(cursor.getColumnIndex("month"));
            int gDay = cursor.getInt(cursor.getColumnIndex("day"));
            arrayList.add(
                    new BillVO(gottenBId, gottenIncome, gottenExpend, gYear, gMonth, gDay,
                            gottenIS, gottenED, gottenBackup
                    )
            );
        }
        cursor.close();
        if (arrayList != null && arrayList.size() > 0){
            return arrayList;
        }
        return null;
    }

    /**
     * @param newBill
     */
    public void updateBill(BillVO newBill){
        SQLiteDatabase db = mDBOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("income", newBill.getIncome());
        values.put("expend", newBill.getExpend());
        values.put("incomeSource", newBill.getIncomeSource());
        values.put("expendDes", newBill.getExpendDes());
        values.put("backup",newBill.getBackup());
        values.put("year",newBill.getYear());
        values.put("month",newBill.getMonth());
        values.put("day",newBill.getDay());

        db.update(BillDBHelper.TABLE_BILL, values,
                "billId=?", new String[]{String.valueOf(newBill.getBillId())});
    }

    /**
     * 按月搜索
     * @param currentMonth
     * @return
     */
    public ArrayList<BillVO> getBillByMonth(int currentMonth){
        SQLiteDatabase db = mDBOpenHelper.getWritableDatabase();
        ArrayList<BillVO> retList = new ArrayList<BillVO>();
        String[] columns = {"billId", "income", "expend", "incomeSource", "expendDes",
                "backup", "year", "month", "day"
        };
        String selections = "month=?";
        String[] selectionArgs = {String.valueOf(currentMonth)};
        Cursor cursor = db.query(BillDBHelper.TABLE_BILL, columns, selections,
                selectionArgs, null, null, null);
        while (cursor.moveToNext()){
            int gottenBId = cursor.getInt(cursor.getColumnIndex("billId"));
            int gottenIncome = cursor.getInt(cursor.getColumnIndex("income"));
            int gottenExpend = cursor.getInt(cursor.getColumnIndex("expend"));
            String gottenIS = cursor.getString(cursor.getColumnIndex("incomeSource"));
            String gottenED = cursor.getString(cursor.getColumnIndex("expendDes"));
            String gottenBackup = cursor.getString(cursor.getColumnIndex("backup"));
            int gYear = cursor.getInt(cursor.getColumnIndex("year"));
            int gMonth = cursor.getInt(cursor.getColumnIndex("month"));
            int gDay = cursor.getInt(cursor.getColumnIndex("day"));
            retList.add(new BillVO(gottenBId, gottenIncome, gottenExpend, gYear, gMonth, gDay,
                            gottenIS, gottenED, gottenBackup)
            );
        }
        cursor.close();
        if (retList != null && retList.size() > 0) {
            return retList;
        }
        return null;
    }

    /**
     * 按天查看
     * @param currentDay
     * @return
     */
    public ArrayList<BillVO> getBillByDay(int currentDay){
        SQLiteDatabase db = mDBOpenHelper.getWritableDatabase();
        ArrayList<BillVO> retList = new ArrayList<BillVO>();
        String[] columns = {"billId", "income", "expend", "incomeSource", "expendDes",
                "backup", "year", "month", "day"
        };
        String selections = "day=?";
        String[] selectionArgs = {String.valueOf(currentDay)};
        Cursor cursor = db.query(BillDBHelper.TABLE_BILL, columns, selections,
                selectionArgs, null, null, null);
        while (cursor.moveToNext()){
            int gottenBId = cursor.getInt(cursor.getColumnIndex("billId"));
            int gottenIncome = cursor.getInt(cursor.getColumnIndex("income"));
            int gottenExpend = cursor.getInt(cursor.getColumnIndex("expend"));
            String gottenIS = cursor.getString(cursor.getColumnIndex("incomeSource"));
            String gottenED = cursor.getString(cursor.getColumnIndex("expendDes"));
            String gottenBackup = cursor.getString(cursor.getColumnIndex("backup"));
            int gYear = cursor.getInt(cursor.getColumnIndex("year"));
            int gMonth = cursor.getInt(cursor.getColumnIndex("month"));
            int gDay = cursor.getInt(cursor.getColumnIndex("day"));
            retList.add(new BillVO(gottenBId, gottenIncome, gottenExpend, gYear, gMonth, gDay,
                    gottenIS, gottenED, gottenBackup)
            );
        }
        cursor.close();
        if (retList != null && retList.size() > 0) {
            return retList;
        }
        return null;
    }


    /**
     *
     * @param billId
     */
    public void deleteBillById(int billId){
        SQLiteDatabase db = mDBOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            String whereClause = "billId=?";
            String[] whereArgs = {String.valueOf(billId)};
            db.delete(BillDBHelper.TABLE_BILL, whereClause, whereArgs);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
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
