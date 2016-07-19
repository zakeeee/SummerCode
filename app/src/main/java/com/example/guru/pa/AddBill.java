package com.example.guru.pa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.suitebuilder.TestMethod;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);
    }

    public void saveBill(View view) {
        incomeText = (EditText)findViewById(R.id.addbill_income);
        incomeSourceText = (EditText)findViewById(R.id.addbill_incomesource);
        expendText = (EditText)findViewById(R.id.addbill_expend);
        expendDesText = (EditText)findViewById(R.id.addbill_expendpurpose);
        calendarView = (CalendarView)findViewById(R.id.calendarView);

        incomeStr = incomeText.getText().toString();
        incomeSourceStr = incomeSourceText.getText().toString();
        expendStr = expendText.getText().toString();
        expendDesStr = expendDesText.getText().toString();

    }

    public void cancelBill(View view) {
        onBackPressed();
    }

}
