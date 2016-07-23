package com.example.guru.pa;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

public class MoneyManage extends AppCompatActivity {

    private BillDBOperator mDBOperator;
    private ArrayList<BillVO> mDayBill;
    private ArrayList<BillVO> mMonthBill;
    /**
     * index == 0 --> day accumulation
     * index == 1 --> month accumulation
     */
    private long[] mTotalIncome = new long[2];
    private long[] mTotalExpend = new long[2];
    private long profit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_manage);

        /* ActionBar添加返回按钮 */
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onResume() {
        super.onResume();

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearlayout_buttons);
        Button button = (Button) linearLayout.findViewById(R.id.jiyibi);

        if(button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MoneyManage.this, AddBill.class);
                    startActivity(intent);
                }
            });
        }

        mDBOperator = new BillDBOperator(this);
        int thisMonth = Integer.parseInt((new SimpleDateFormat("MM")).format(
                new Date(System.currentTimeMillis())
        ));
        int today = Integer.parseInt((new SimpleDateFormat("dd")).format(
                new Date(System.currentTimeMillis())
        ));
        mDayBill = mDBOperator.getBillByDay(today);
        mMonthBill = mDBOperator.getBillByMonth(thisMonth);
        mTotalIncome[0] = mTotalIncome[1]
                = mTotalExpend[0] = mTotalExpend[1] = 0;

        if (mDayBill != null && mDayBill.size() > 0) {
            for (int i = 0; i < mDayBill.size(); ++ i) {
                mTotalIncome[0] += mDayBill.get(i).getIncome();
                mTotalExpend[0] += mDayBill.get(i).getExpend();
            }
        }

        if (mMonthBill != null && mMonthBill.size() > 0) {
            for (int i = 0; i < mMonthBill.size(); ++ i) {
                mTotalIncome[1] += mMonthBill.get(i).getIncome();
                mTotalExpend[1] += mMonthBill.get(i).getExpend();
            }
        }

        profit = mTotalIncome[1] - mTotalExpend[1];
        TextView viewOfIncome = (TextView)findViewById(R.id.income_this_month);
        TextView viewOfExpend = (TextView)findViewById(R.id.expend_this_month);
        TextView viewOfProfit = (TextView)findViewById(R.id.accumulateProfit);
        TextView viewOfDayIncome = (TextView)findViewById(R.id.day_income);
        TextView viewOfDayExpend = (TextView)findViewById(R.id.day_expend);
        viewOfDayExpend.setText(mTotalExpend[0] + "");
        viewOfDayIncome.setText(mTotalIncome[0] + "");
        viewOfIncome.setText(mTotalIncome[1] + "");
        viewOfExpend.setText(mTotalExpend[1] + "");
        viewOfProfit.setText(profit + "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_money,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.bill_list:
                Intent intent=new Intent(MoneyManage.this,Bill.class);
                startActivity(intent);
                break;
            case android.R.id.home:
                this.finish();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }
}
