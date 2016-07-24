package com.example.guru.pa;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class ChartView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_chart_view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //SetInfo(String[] XLabels,String[] YLabels,String[] DataIncome,String[] DataExpend,String strTitle)
        DesignedChartView chartView = new DesignedChartView(ChartView.this);
        chartView.SetInfo(new String[] { "6-6", "6-7", "6-8", "6-9", "6-10", "6-11", "6-12" },
                new String[] { "","50", "100", "150", "200", "250", "300", "350","400"},
                new String[] { "123", "333", "88", "76", "280", "70","288"},
                new String[] { "55", "84", "133", "114", "90","320","155"},
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
