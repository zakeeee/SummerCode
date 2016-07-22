package com.example.guru.pa;

/**
 * Created by Haoyu on 2016/7/22.
 */
public class BillDetail {

    private int billId;
    private int day;
    private int month;
    private int year;
    private String backup;

    public BillDetail() {}

    public BillDetail(int billId, int year, int month, int day, String backup) {
        this.billId = billId;
        this.backup = backup;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public String getBackup() {
        return backup;
    }

    public int getBillId() {
        return billId;
    }


    public void setBackup(String backup) {
        this.backup = backup;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
