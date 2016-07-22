package com.example.guru.pa;

/**
 * Created by Haoyu on 2016/7/22.
 */
public class BillVO {

    private int billId;
    private int income;
    private int expend;
    private int day;
    private int month;
    private int year;
    private String backup;
    private String incomeSource;
    private String expendDes;

    public BillVO() {}
    public BillVO(int billId, int income, int expend, int year, int month, int day,
                  String incomeSource, String expendDes, String backup){
        this.billId = billId;
        this.income = income;
        this.expend = expend;
        this.incomeSource = incomeSource;
        this.expendDes = expendDes;
        this.backup = backup;
        this.year = year;
        this.month = month;
        this.day = day;
    }


    public int getBillId() {
        return billId;
    }

    public int getIncome() {
        return  income;
    }

    public int getExpend() {
        return expend;
    }

    public String getIncomeSource() {
        return incomeSource;
    }

    public String getExpendDes() {
        return expendDes;
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


    public void setBackup(String backup) {
        this.backup = backup;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public void setIncome(int income) {
        this.income = income;
    }

    public void setExpend(int expend) {
        this.expend = expend;
    }

    public void setIncomeSource(String incomeSource) {
        this.incomeSource = incomeSource;
    }

    public void setExpendDes(String expendDes) {
        this.expendDes = expendDes;
    }



}
