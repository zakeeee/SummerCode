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
import java.util.regex.Pattern;

public class MoneyManage extends AppCompatActivity {

    private String fileContent = null;
    private String[] lineContent = null;
    private String[] elementContent = null;
    private long[] total = new long[2];
    private int indexOftotal;
    // index == 0 --> income, index == 1 --> expend
    // 来源与目的的标签和内容之间没有加空格，所以只有两个地方会被识别为整数
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

        FileOperate fileOperate = new FileOperate(this);
        fileOperate.ifFileExist(MainActivity.FILENAME);
        try {
            fileContent = fileOperate.read(MainActivity.FILENAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
        lineContent = fileContent.split("\n");
        total[0] = total[1] = profit = 0;

        for (String s : lineContent){
            // Log.e("TestNumber", s);
            indexOftotal = 0;
            elementContent = s.split(" ");
            for (String element : elementContent){
                // Log.e("TestNumber", element);
                if (isNumeric(element) && !element.equals("")){

                    total[indexOftotal++] += Long.parseLong(element);
                }
            }
        }
        profit = total[0] - total[1];
        TextView viewOfIncome = (TextView)findViewById(R.id.income_this_month);
        TextView viewOfExpend = (TextView)findViewById(R.id.expend_this_month);
        TextView viewOfProfit = (TextView)findViewById(R.id.accumulateProfit);
        viewOfIncome.setText(total[0] + "");
        viewOfExpend.setText(total[1] + "");
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
                Intent intent = new Intent(MoneyManage.this, Bill.class);
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
