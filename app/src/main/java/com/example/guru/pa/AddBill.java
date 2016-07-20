package com.example.guru.pa;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;

public class AddBill extends AppCompatActivity {

    private EditText incomeText = null;
    private EditText incomeSourceText = null;
    private EditText expendText = null;
    private EditText expendDesText = null;
    private CalendarView calendarView = null;
    private String incomeStr = null;
    private String incomeSourceStr = null;
    private String expendStr = null;
    private String expendDesStr = null;
    private SimpleDateFormat dateFormat = null;
    private String calendarDate = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);
        // 获得当前日历选中的日期
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        calendarView = (CalendarView)findViewById(R.id.calendarView);
        calendarDate = "Time: " + dateFormat.format(calendarView.getDate());
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                calendarDate = "Time: " + String.valueOf(year) + "-"
                        + String.valueOf(month + 1) + "-" + String.valueOf(dayOfMonth);
            }
        });
    }

    public void saveBill(View view) throws Exception {
        incomeText = (EditText)findViewById(R.id.addbill_income);
        incomeSourceText = (EditText)findViewById(R.id.addbill_incomesource);
        expendText = (EditText)findViewById(R.id.addbill_expend);
        expendDesText = (EditText)findViewById(R.id.addbill_expendpurpose);

        incomeStr = " 收入(元): " + incomeText.getText().toString();
        incomeSourceStr = " 来源:" + incomeSourceText.getText().toString(); // 两者之间没有空格
        expendStr = " 支出(元): " + expendText.getText().toString();
        expendDesStr = " 目的:" + expendDesText.getText().toString();     // 两者之间没有空格


        FileOperate fileOperate = new FileOperate(this);
        String bufferContent = calendarDate + " "  + incomeStr + " "
                + incomeSourceStr + " " + expendStr + " " + expendDesStr;
        fileOperate.save(MainActivity.FILENAME, bufferContent);
        /**
         * Test Read&Write File class
         */
        //String bufferRead = fileOperate.read(filename);
        //Log.e("ReadTest",bufferRead);
        Intent intent = new Intent(this, Bill.class);
        startActivity(intent);
    }

    public void cancelBill(View view) {
        onBackPressed();
    }

}
