package com.example.guru.pa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Crystal on 2016/7/22.
 */
public class PasswordOperate {
    PasswordDBHelper mpasswordDBHelper;
    private static String DBNAME = "mypassword.db";
    public PasswordOperate(Context context){
        mpasswordDBHelper = new PasswordDBHelper(context, DBNAME);
    }

    public void save(PasswordMessage passwordMessage){
        SQLiteDatabase db = mpasswordDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("purpose", passwordMessage.getPurpose());
        values.put("username", passwordMessage.getUsername());
        values.put("password", passwordMessage.getPassword());
        values.put("extra", passwordMessage.getExtra());
        db.insert(PasswordDBHelper.TABLE, null, values);
    }
 /*   public int save(PasswordMessage passwordMessage){
        SQLiteDatabase db = mpasswordDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("purpose", passwordMessage.getPurpose());
        values.put("username", passwordMessage.getUsername());
        values.put("password", passwordMessage.getPassword());
        values.put("extra", passwordMessage.getExtra());
        db.beginTransaction();
        int id = -1;
        try {
            String sql = "select max(id) from" + " " + PasswordDBHelper.TABLE;
            db.insert(PasswordDBHelper.TABLE, null, values);
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()){
                id = (int)cursor.getLong(0);
            }
            cursor.close();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return id;
    }*/

    public PasswordMessage getByid(Integer id){
        SQLiteDatabase db = mpasswordDBHelper.getWritableDatabase();
        String[] columns = {"id", "purpose", "username", "password", "extra"};
        String selections = "id=?";
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(mpasswordDBHelper.TABLE, columns,
                selections ,selectionArgs, null, null, null);
        if (cursor.moveToFirst()){
            int gottenId = cursor.getInt(cursor.getColumnIndex("id"));
            String gottenPurpose = cursor.getString(cursor.getColumnIndex("purpose"));
            String gottenUsername = cursor.getString(cursor.getColumnIndex("username"));
            String gottenPassword = cursor.getString(cursor.getColumnIndex("password"));
            String gottenExtra = cursor.getString(cursor.getColumnIndex("extra"));
            cursor.close();
            return new PasswordMessage(gottenId, true, gottenPurpose, gottenUsername, gottenPassword, gottenExtra);
        }
        cursor.close();
        return null;
    }

    public ArrayList<PasswordMessage> getAllPasswordMessage() {
        ArrayList<PasswordMessage> arrayList= new ArrayList<PasswordMessage>();
        SQLiteDatabase db = mpasswordDBHelper.getWritableDatabase();
        String[] columns = {"id", "purpose", "username", "password", "extra"};
        Cursor cursor = db.query(PasswordDBHelper.TABLE, columns,
                null, null, null, null, null);
        while (cursor.moveToNext()){
            int gottenId = cursor.getInt(cursor.getColumnIndex("id"));
            String gottenPurpose = cursor.getString(cursor.getColumnIndex("purpose"));
            String gottenUsername = cursor.getString(cursor.getColumnIndex("username"));
            String gottenPassword = cursor.getString(cursor.getColumnIndex("password"));
            String gottenExtra = cursor.getString(cursor.getColumnIndex("extra"));
            arrayList.add(
                 new PasswordMessage(gottenId,true, gottenPurpose, gottenUsername, gottenPassword, gottenExtra)
            );
        }
        cursor.close();
        if (arrayList != null && arrayList.size() > 0){
            return arrayList;
        }
        return null;
    }

    public void deleteByid(int id){
        SQLiteDatabase db = mpasswordDBHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            String whereClause = "id=?";
            String[] whereArgs = {String.valueOf(id)};
            db.delete(PasswordDBHelper.TABLE, whereClause, whereArgs);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void update(PasswordMessage newPasswordMessage){
        SQLiteDatabase db = mpasswordDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("purpose", newPasswordMessage.getPurpose());
        values.put("username", newPasswordMessage.getUsername());
        values.put("password", newPasswordMessage.getPassword());
        values.put("extra", newPasswordMessage.getExtra());
        values.put("id", newPasswordMessage.getId());
        db.update(PasswordDBHelper.TABLE, values,
                "id=?", new String[]{String.valueOf(newPasswordMessage.getId())});
    }

    public void closeDB() {
        if (mpasswordDBHelper != null){
            mpasswordDBHelper.close();
        }
    }

}
