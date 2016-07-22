package com.example.guru.pa;

/**
 * Created by Haoyu on 2016/7/22.
 */
public class BillVO {

    private int billId;
    private int income;
    private int expend;
    private String incomeSource;
    private String expendDes;
  //  private String date;

    public BillVO() {}
    public BillVO(int billId, int income, int expend, String incomeSource, String expendDes
           // , String date
    ){
        this.billId = billId;
        this.income = income;
        this.expend = expend;
        this.incomeSource = incomeSource;
        this.expendDes = expendDes;
        //this.date = date;

    }
/*
    public String getDate() {
        return date;
    }
*/
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

    public void setDate(String date) {
      //  this.date = date;
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
