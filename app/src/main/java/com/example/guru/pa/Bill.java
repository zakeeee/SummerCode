package com.example.guru.pa;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/*import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;*/

import java.io.IOException;
import java.util.List;

public class Bill extends AppCompatActivity {

    private List<ApplicationInfo> mAppList;
    private RelativeLayout layout = null;
    private String fileContent = null;
    private String[] lineContent;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        FileOperate fileOperate = new FileOperate(this);
        fileOperate.ifFileExist(MainActivity.FILENAME);
        try {
            fileContent = fileOperate.read(MainActivity.FILENAME);
        } catch (IOException e) {
            e.printStackTrace();
        }

        layout = new RelativeLayout(this);
        //按行拆分
        lineContent = fileContent.split("\n");
        int index = 0;
        for (String s : lineContent){
            displayContent(s,index++);
        }
        setContentView(layout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_bill,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.bill_plus){
            Intent intent = new Intent(Bill.this, AddBill.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void displayContent(String s,int index){

       // Log.e("TestSlipt",i + "  " + s);
        textView = new TextView(this);
        textView.setId(index);
        textView.setTextColor(Color.BLACK);
        textView.setBackgroundColor(Color.WHITE);
        textView.setHeight(120);
        textView.setText(s);
        RelativeLayout.LayoutParams param =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        if (index > 1){
            param.addRule(RelativeLayout.BELOW, index - 1);
        }
        layout.addView(textView,param);
    }
}
