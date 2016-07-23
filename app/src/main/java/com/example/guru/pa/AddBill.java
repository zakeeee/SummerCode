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
import java.text.ParseException;
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
    private int gottenId;

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
    public void onResume() {
        super.onResume();
        incomeText = (EditText)findViewById(R.id.addbill_income);
        incomeSourceText = (EditText)findViewById(R.id.addbill_incomesource);
        expendText = (EditText)findViewById(R.id.addbill_expend);
        expendDesText = (EditText)findViewById(R.id.addbill_expendpurpose);
        mBackupText = (EditText)findViewById(R.id.addbill_extra);
        /**
         * 初始化界面（若为编辑状态）
         */
        Intent intent = getIntent();
        gottenId = -1;
        if (intent != null) {
            gottenId = intent.getIntExtra("pa.money.detail.edit", -1);

            if (gottenId > 0) {
                BillDBOperator initDB = new BillDBOperator(this);
                BillVO initBill = initDB.getBillById(gottenId);

                SimpleDateFormat sdfDate = new SimpleDateFormat("yy-MM-dd");
                SimpleDateFormat sdfHour = new SimpleDateFormat("HH");
                SimpleDateFormat sdfMinute = new SimpleDateFormat("mm");
                long sDate = 0;
                int sHour = 0;
                int sMinute = 0;
                if (initBill != null) {
                    String date = initBill.getDate();
                    String backup = initBill.getBackup();
                    try {
                        sDate = sdfDate.parse(date).getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    calendarView.setDate(sDate);
                    incomeText.setText(initBill.getIncome() + "");
                    incomeSourceText.setText(initBill.getIncomeSource());
                    expendText.setText(initBill.getExpend() + "");
                    expendDesText.setText(initBill.getExpendDes());
                    mBackupText.setText(initBill.getBackup());
                }

            }
        }

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
        if (incomeText.getText().toString().equals("") || expendText.getText().toString().equals("")){
            Toast.makeText(AddBill.this,"收入或支出不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        incomeStr = incomeText.getText().toString();
        incomeSourceStr = incomeSourceText.getText().toString();
        expendStr = expendText.getText().toString();
        expendDesStr = expendDesText.getText().toString();
        mBackup = mBackupText.getText().toString();


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

        if (gottenId > 0) {
            billVO.setBillId(gottenId);
            mDBOperator.updateBill(billVO);
        }
        else {
            int billId = mDBOperator.saveBill(billVO);
        }


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
