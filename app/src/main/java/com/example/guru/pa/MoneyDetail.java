package com.example.guru.pa;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class MoneyDetail extends AppCompatActivity {


    private BillDBOperator mDBOperator;
    private int billId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_edit,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (intent != null) {
            billId = intent.getIntExtra(MainActivity.MESSAGE_BILL, 0);

            mDBOperator = new BillDBOperator(this);
            BillVO gottenBill = mDBOperator.getBillById(billId);
            TextView date = (TextView)findViewById(R.id.money_detail_date);
            TextView income = (TextView)findViewById(R.id.money_detail_income);
            TextView expend = (TextView)findViewById(R.id.money_detail_expend);
            TextView incomeSource = (TextView)findViewById(R.id.money_detail_income_res);
            TextView expendDes = (TextView)findViewById(R.id.money_detail_expend_purpose);
            TextView backup = (TextView)findViewById(R.id.money_detail_income_backup);

            date.setText(gottenBill.getYear() + "-" + gottenBill.getMonth() + "-" + gottenBill.getDay());
            income.setText("￥ " + gottenBill.getIncome());
            expend.setText("￥ " + gottenBill.getExpend());
            incomeSource.setText("收入来源:\n" + gottenBill.getIncomeSource());
            expendDes.setText("支出目的:\n" + gottenBill.getExpendDes());
            backup.setText("备注：\n" + gottenBill.getBackup());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.edit:
                sendId();
                break;
            case android.R.id.home:
                this.finish();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendId() {
        Intent intent = new Intent(MoneyDetail.this, AddBill.class);
        intent.putExtra(MainActivity.MESSAGE_BILL, billId);
        startActivity(intent);
    }

}
