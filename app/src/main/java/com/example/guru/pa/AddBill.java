package com.example.guru.pa;

import android.content.Context;
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
   // private SimpleDateFormat dateFormat = null;
    private String calendarDate = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);

        calendarView = (CalendarView)findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                calendarDate = String.valueOf(year) + "-"
                        + String.valueOf(month + 1) + "-" + String.valueOf(dayOfMonth);
            }
        });
    }

    public void saveBill(View view) throws Exception {
        incomeText = (EditText)findViewById(R.id.addbill_income);
        incomeSourceText = (EditText)findViewById(R.id.addbill_incomesource);
        expendText = (EditText)findViewById(R.id.addbill_expend);
        expendDesText = (EditText)findViewById(R.id.addbill_expendpurpose);



        incomeStr = incomeText.getText().toString();
        incomeSourceStr = incomeSourceText.getText().toString();
        expendStr = expendText.getText().toString();
        expendDesStr = expendDesText.getText().toString();
        //calendarDate = dateFormat.format(calendarView.getDate());

        String filename = "testFile";
        FileOperate fileOperate = new FileOperate(this);
        String bufferContent = calendarDate + "\n"  + incomeStr + "\n"
                + incomeSourceStr + "\n" + expendStr + "\n" + expendDesStr;
        fileOperate.save(filename, bufferContent);

        String bufferRead = fileOperate.read(filename);
        Log.e("ReadTest",bufferRead);
    }

    public void cancelBill(View view) {
        onBackPressed();
    }

}
