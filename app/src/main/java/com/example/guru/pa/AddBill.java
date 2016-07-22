package com.example.guru.pa;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;

public class AddBill extends AppCompatActivity {

    private EditText incomeText = null;
    private EditText incomeSourceText = null;
    private EditText expendText = null;
    private EditText expendDesText = null;
    private EditText mBackupText = null;
    private CalendarView calendarView = null;
    private String incomeStr = null;
    private String incomeSourceStr = null;
    private String expendStr = null;
    private String expendDesStr = null;
    private String mBackup = null;
   // private String calendarDate = null;
    private SimpleDateFormat dateFormat = null;
    private int mYear;
    private int mMonth;
    private int mDay;
    private BillDBOperator mDBOperator = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);

        /* ActionBar添加返回按钮 */
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // 获得当前日历选中的日期
        //dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        calendarView = (CalendarView)findViewById(R.id.calendarView);
       // calendarDate = dateFormat.format(calendarView.getDate());
        mYear = Integer.parseInt((new SimpleDateFormat("yyyy")).format(calendarView.getDate()));
        mMonth = Integer.parseInt((new SimpleDateFormat("MM")).format(calendarView.getDate()));
        mDay = Integer.parseInt((new SimpleDateFormat("dd")).format(calendarView.getDate()));
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {

                mYear = year;
                mMonth = month + 1;
                mDay = dayOfMonth;
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveBill(View view) throws Exception {
        incomeText = (EditText)findViewById(R.id.addbill_income);
        incomeSourceText = (EditText)findViewById(R.id.addbill_incomesource);
        expendText = (EditText)findViewById(R.id.addbill_expend);
        expendDesText = (EditText)findViewById(R.id.addbill_expendpurpose);
        mBackupText = (EditText)findViewById(R.id.addbill_extra);

        if (incomeText.getText().toString().equals("") || expendText.getText().toString().equals("")){
            Toast.makeText(AddBill.this,"收入或支出不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        incomeStr =// " 收入(元): " +
                incomeText.getText().toString();
        incomeSourceStr = //" 来源:" +
                incomeSourceText.getText().toString(); // 两者之间没有空格
        expendStr =// " 支出(元): " +
                expendText.getText().toString();
        expendDesStr = //" 目的:" +
                expendDesText.getText().toString();     // 两者之间没有空格
        mBackup =
                mBackupText.getText().toString();


        mDBOperator = new BillDBOperator(this);
        BillVO billVO = new BillVO();
        billVO.setIncome(Integer.parseInt(incomeStr));
        billVO.setIncomeSource(incomeSourceStr);
        billVO.setExpend(Integer.parseInt(expendStr));
        billVO.setExpendDes(expendDesStr);
        billVO.setBackup(mBackup);
        billVO.setDay(mDay);
        billVO.setMonth(mMonth);
        billVO.setYear(mYear);

        int billId = mDBOperator.saveBill(billVO);



        this.finish();
        //Intent intent = new Intent(this, Bill.class);
        //startActivity(intent);
    }

    public void cancelBill(View view) {
        onBackPressed();
    }


    public void debug(int billId) {

    }
}
