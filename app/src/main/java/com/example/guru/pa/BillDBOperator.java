package com.example.guru.pa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

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
       // values.put("date", bill.getDate());

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
     *
     * @param billDetail
     * @return
     */
    public int saveDetail(BillDetail billDetail){
        SQLiteDatabase db = mDBOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("backup",billDetail.getBackup());
        values.put("year",billDetail.getYear());
        values.put("month",billDetail.getMonth());
        values.put("day",billDetail.getDay());
        values.put("billId",billDetail.getBillId());

        db.beginTransaction();
        int detailId = -1;
        try {
            String sql = "select max(Id) from" + " " + BillDBHelper.TABLE_DETAIL;
            db.insert(BillDBHelper.TABLE_DETAIL, null, values);
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()){
                detailId = (int)cursor.getLong(0);
            }
            cursor.close();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return detailId;
    }

    /**
     * @param billId
     * @return
     */
    public BillVO getBillById(int billId){
        SQLiteDatabase db = mDBOpenHelper.getWritableDatabase();
        String[] columns = {"billId", "income", "expend", "incomeSource", "expendDes"
             //   , "date"
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
           // String gottenDate = cursor.getString(cursor.getColumnIndex("date"));

            cursor.close();
            return new BillVO(gottenBId, gottenIncome, gottenExpend,
                    gottenIS, gottenED
                  //  , gottenDate
            );
        }
        cursor.close();
        return null;
    }

    /**
     *
     * @param detailId
     * @return
     */
    public BillDetail getDetailById(int detailId) {
        SQLiteDatabase db = mDBOpenHelper.getWritableDatabase();
        String[] columns = {"billId", "backup", "year", "month", "day"};
        String selections = "billId=?";
        String[] selectionArgs = {String.valueOf(detailId)};
        Cursor cursor = db.query(BillDBHelper.TABLE_DETAIL, columns,
                selections ,selectionArgs, null, null, null);
        if (cursor.moveToFirst()){
            int gottenBId = cursor.getInt(cursor.getColumnIndex("billId"));
            String gottenBackup = cursor.getString(cursor.getColumnIndex("backup"));
            int gYear = cursor.getInt(cursor.getColumnIndex("year"));
            int gMonth = cursor.getInt(cursor.getColumnIndex("month"));
            int gDay = cursor.getInt(cursor.getColumnIndex("day"));

            cursor.close();
            return new BillDetail(gottenBId, gYear, gMonth, gDay, gottenBackup);
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
        String[] columns = {"billId", "income", "expend", "incomeSource", "expendDes"
              //  , "date"
        };
        Cursor cursor = db.query(BillDBHelper.TABLE_BILL, columns,
                null, null, null, null, null);
        while (cursor.moveToNext()){
            int gottenBId = cursor.getInt(cursor.getColumnIndex("billId"));
            int gottenIncome = cursor.getInt(cursor.getColumnIndex("income"));
            int gottenExpend = cursor.getInt(cursor.getColumnIndex("income"));
            String gottenIS = cursor.getString(cursor.getColumnIndex("incomeSource"));
            String gottenED = cursor.getString(cursor.getColumnIndex("expendDes"));
           // String gottenDate = cursor.getString(cursor.getColumnIndex("date"));
            arrayList.add(
                    new BillVO(gottenBId, gottenIncome, gottenExpend,
                            gottenIS, gottenED
                          //  , gottenDate
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
       // values.put("date", newBill.getDate());

        db.update(BillDBHelper.TABLE_BILL, values,
                "billId=?", new String[]{String.valueOf(newBill.getBillId())});
    }

    /**
     *
     * @param billDetail
     */
    public void updateDetail(BillDetail billDetail){
        SQLiteDatabase db = mDBOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("billId",billDetail.getBillId());
        values.put("backup",billDetail.getBackup());
        values.put("year",billDetail.getYear());
        values.put("month",billDetail.getMonth());
        values.put("day",billDetail.getDay());
        values.put("billId",billDetail.getBillId());

        db.update(BillDBHelper.TABLE_DETAIL, values,
                "billId=?", new String[]{String.valueOf(billDetail.getBillId())});
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
