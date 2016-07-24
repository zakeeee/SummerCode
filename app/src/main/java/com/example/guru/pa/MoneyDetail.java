package com.example.guru.pa;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class MoneyDetail extends AppCompatActivity {


    private BillDBOperator mDBOperator;
    private int billId;
    private boolean mLocal;
    private String mDate;
    private int mIncome;
    private int mExpend;
    private String mIncomeSource;
    private String mExpendDes;
    private String mBackup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent != null) {
            /*billId = intent.getIntExtra("pa.bill.detail", 0);
            mDBOperator = new BillDBOperator(this);
            BillVO gottenBill = mDBOperator.getBillById(billId);*/
            billId = intent.getIntExtra("billId",-1);
            mLocal = intent.getBooleanExtra("local", true);
            mDate = intent.getStringExtra("date");
            mIncome = intent.getIntExtra("income",0);
            mExpend = intent.getIntExtra("expend",0);
            mIncomeSource = intent.getStringExtra("incomeSource");
            mExpendDes = intent.getStringExtra("expengDes");
            mBackup = intent.getStringExtra("backup");

            setData();
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_edit,menu);
        return super.onCreateOptionsMenu(menu);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==2 && requestCode==1){
            billId = data.getIntExtra("billId",-1);
            mLocal = data.getBooleanExtra("local", true);
            mDate = data.getStringExtra("date");
            Log.e("hh",mDate);
            mIncome = data.getIntExtra("income",0);
            Log.e("hh2",String.valueOf(mIncome));
            mExpend = data.getIntExtra("expend",0);
            mIncomeSource = data.getStringExtra("incomeSource");
            mExpendDes = data.getStringExtra("expengDes");
            mBackup = data.getStringExtra("backup");

            setData();
        }
    }


    private void setData() {
        TextView date = (TextView)findViewById(R.id.money_detail_date);
        TextView income = (TextView)findViewById(R.id.money_detail_income);
        TextView expend = (TextView)findViewById(R.id.money_detail_expend);
        TextView incomeSource = (TextView)findViewById(R.id.money_detail_income_res);
        TextView expendDes = (TextView)findViewById(R.id.money_detail_expend_purpose);
        TextView backup = (TextView)findViewById(R.id.money_detail_income_backup);

        date.setText(mDate);
        income.setText("￥ " + mIncome);
        expend.setText("￥ " + mExpend);
        incomeSource.setText("收入来源:\n" + mIncomeSource);
        expendDes.setText("支出目的:\n" + mExpendDes);
        backup.setText("备注：\n" + mBackup);
    }


    public void sendId() {
        /*Intent intent = new Intent(MoneyDetail.this, AddBill.class);
        intent.putExtra("pa.money.detail.edit", billId);
        startActivity(intent);*/
        Intent  intent = new Intent(MoneyDetail.this, AddBill.class);
        intent.putExtra("billId",billId);
        intent.putExtra("local", mLocal);
        intent.putExtra("date", mDate);
        intent.putExtra("income", mIncome);
        intent.putExtra("incomeSource", mIncomeSource);
        intent.putExtra("expend", mExpend);
        intent.putExtra("expendDes", mExpendDes);
        intent.putExtra("backup", mBackup);
        startActivityForResult(intent, 1);
    }

}
