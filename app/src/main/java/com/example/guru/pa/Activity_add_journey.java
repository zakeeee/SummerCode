package com.example.guru.pa;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class Activity_add_journey extends AppCompatActivity {

    private Spinner spinner_T;
    private Spinner spinner_Way;
    private ArrayAdapter<String> adapter_T;
    private ArrayAdapter<String> adapter_Way;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_add_journey);

        /* ActionBar添加返回按钮 */
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // 初始化控件
        spinner_T = (Spinner) findViewById(R.id.journey_T);
        spinner_Way = (Spinner) findViewById(R.id.journey_Way);
        // 建立数据源
        String[] Items1 = getResources().getStringArray(R.array.spinner_T);
        String[] Items2 = getResources().getStringArray(R.array.spinner_Way);
        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> _Adapter1=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, Items1);
        ArrayAdapter<String> _Adapter2=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, Items2);
        //绑定 Adapter到控件
        spinner_T.setAdapter(_Adapter1);
        spinner_Way.setAdapter(_Adapter2);
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
}
