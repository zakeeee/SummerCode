package com.example.guru.pa;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChartView extends AppCompatActivity {

    private static final int LENGTH = 12;
    private String[] mXLabel;
    private String[] mYLabel;
    private String[] mIncome;
    private String[] mExpend;
    private BillDBOperator mBillDBOperator;
    private SimpleDateFormat mDateFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_chart_view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //SetInfo(String[] XLabels,String[] YLabels,String[] DataIncome,String[] DataExpend,String strTitle)

    }

    @Override
    public void onResume() {
        super.onResume();
        String[] mXLabel = new String[LENGTH];
        String[] mYLabel = new String[LENGTH];
        String[] mIncome = new String[LENGTH];
        String[] mExpend = new String[LENGTH];
        mBillDBOperator = new BillDBOperator(this);
        mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = mDateFormat.format(new Date());
        String em[] = today.split("-");

        long max = 0;
        long min = 0;
        for (int i = 0; i < LENGTH; ++ i) {
            int month = Integer.parseInt(em[1]) - i;
            int year = Integer.parseInt(em[0]);
            if (month <= 0) {
                month += LENGTH;
                -- year;
            }
            String curDay = year + "-" + month;
            int index = LENGTH - i - 1;

            mXLabel[index] = curDay;
            ArrayList<BillVO> gottenList = mBillDBOperator.getBillByMonth(month);
            if (gottenList == null || gottenList.size() == 0){
                mIncome[index] = "0";
                mExpend[index] = "0";
            }
            else {
                long income = 0;
                long expend = 0;
                for (int j = 0; j < gottenList.size(); ++ j) {
                    income += gottenList.get(j).getIncome();
                    expend += gottenList.get(j).getExpend();
                }
                mIncome[index] = income + "";
                mExpend[index] = expend + "";
                long tmax = income > expend ? income : expend;
                long tmin = income < expend ? income : expend;
                max = tmax > max ? tmax : max;
                min = tmin < min ? tmin : min;
            }
        }
        long step = (max - min) / 10;
        for (int i = 0; i < 10; ++ i) {
            mYLabel[i] = min + "";
            min += step;
        }

        DesignedChartView chartView = new DesignedChartView(ChartView.this);
        chartView.SetInfo(mXLabel, mYLabel, mIncome, mExpend,
                "收入支出曲线 （收入BLUE 支出RED）");

        setContentView(chartView);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
