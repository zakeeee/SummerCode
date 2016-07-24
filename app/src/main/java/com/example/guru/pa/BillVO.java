package com.example.guru.pa;

/**
 * Created by Haoyu on 2016/7/22.
 */
public class BillVO implements Comparable<BillVO>{

    private int billId;
    private int income;
    private int expend;
    private int day;
    private int month;
    private int year;
    private String backup;
    private String incomeSource;
    private String expendDes;
    private boolean mLocal = true;

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


    public boolean getLocal() { return mLocal; }

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


    public void setLocal(boolean local) { this.mLocal = local; }

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

    public String getDate() {
        String m = month + "";
        String d = day + "";
        if (month < 10) m = "0" + m;
        if (day < 10) d = "0" + d;
        return year + "-" + m + "-" + d;
    }

    @Override
    public String toString() {
        String m = month + "";
        String d = day + "";
        if (month < 10) m = "0" + m;
        if (day < 10) d = "0" + d;
        String str = "";
        if(mLocal) {
            str = "<本地>\n"
                    +"date: " + year + "-" + m + "-" + d + "\n"
                    +"收入: " + income + "\n"
                    +"支出: " + expend;
        } else {
            str = "<云端>\n"
                    +"date: " + year + "-" + m + "-" + d + "\n"
                    +"收入: " + income + "\n"
                    +"支出: " + expend;
        }

        return str;
    }

    @Override
    public int compareTo(BillVO b) {
        int ret = this.getDate().compareTo(b.getDate());
        int reInc = this.getIncome() - b.getIncome();
        if (ret == 0) {
            if (reInc == 0)
                return  this.getExpend() - b.getExpend();
            else
                return reInc;
        }
        else
            return ret;
    }
}
