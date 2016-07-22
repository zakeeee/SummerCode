package com.example.guru.pa;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AddJourney extends AppCompatActivity {

    private Spinner spinner_T;
    private Spinner spinner_Way;
    private ArrayAdapter<String> adapter_T;
    private ArrayAdapter<String> adapter_Way;
    private EditText mBackUp = null;
    private CalendarView mDate = null;
    private TimePicker mTime = null;
    private String mScheduleContent = null;
    private String mGottenDate = null;
    private SimpleDateFormat mDateFormat = null;
    private String mGottenTime = null;
    private DataBaseOperator mDBOperator = null;
    private int mSelectedWayPosition;
    private int mSelectedTPosition;
    private int mYear;
    private int mMonth;
    private int mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journey);

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

        //获得Spinner的状态
        //spinner_T的状态根据spinner_Way的状态而改变
        spinner_Way.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                mSelectedWayPosition = position;
                if (position == 0){
                    spinner_T.setVisibility(View.GONE);
                    mSelectedTPosition = -1;
                }
                else {
                    mSelectedTPosition = spinner_T.getSelectedItemPosition();
                    spinner_T.setVisibility(View.VISIBLE);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });


        // 获得当前日历选中的日期
        mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mDate = (CalendarView)findViewById(R.id.journey_cal);
        mGottenDate = mDateFormat.format(mDate.getDate()); //默认日期
        mDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                mGottenDate = String.valueOf(year) + "-" + String.valueOf(month + 1)
                        + "-" + String.valueOf(dayOfMonth);
                mYear = year;
                mMonth = month + 1;
                mDay = dayOfMonth;
            }
        });
        //获得当前选中的时间
        mTime = (TimePicker)findViewById(R.id.journey_timePicker);
        mGottenTime = new SimpleDateFormat("HH:mm").format(mDate.getDate()); //默认时间
        mTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute){
                mGottenTime = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
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

    public void saveJourney(View view){
        Schedule schedule = new Schedule();
        mDBOperator = new DataBaseOperator(this);
        mBackUp = (EditText)findViewById(R.id.journey_backup);
        mScheduleContent = mBackUp.getText().toString();

        if (mScheduleContent.equals("")){
            Toast.makeText(AddJourney.this,"备注不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        schedule.setContent(mScheduleContent);
        schedule.setDate(mGottenDate);
        schedule.setTime(mGottenTime);
        int scheduleId =  mDBOperator.saveSchedule(schedule);

        int tagId = -1;
        if (mSelectedWayPosition != 0) {
            tagId = saveTagSchedule(scheduleId);
        }

        //test database
       // deBug(scheduleId,tagId);


        //销毁当前activity
        this.finish();
        if (mDBOperator != null){
            mDBOperator.closeDB();
        }
        Intent intent = new Intent(this, JourneyManage.class);
        startActivity(intent);
    }

    public void cancelJourney(View view){
        onBackPressed();
    }

    public void onBackPressed(){

        if (mDBOperator != null){
            mDBOperator.closeDB();
        }
        this.finish();
    }

    public int saveTagSchedule(int scheduleId){
        TagSchedule tagSchedule = new TagSchedule();
        tagSchedule.setScheduleId(scheduleId);
        tagSchedule.setRemindId(mSelectedTPosition);
        tagSchedule.setYear(mYear);
        tagSchedule.setMonth(mMonth);
        tagSchedule.setDay(mDay);
        int tagId = mDBOperator.saveTagSchedule(tagSchedule);
        return tagId;
    }


    //debug
    public void deBug(int scheduleId, int tagId) {
        /*
        Schedule testSchedule = mDBOperator.getScheduleById(scheduleId);
        Log.e("TestDB", testSchedule.getContent() + "\n" +
                testSchedule.getDate() + '\n' + testSchedule.getTime() + "\n" +
                testSchedule.getScheduleId());

        //Log.e("Spinner",mSelectedWayPosition + " " + mSelectedTPosition);
        // Log.e("year and month",mYear + " " + mMonth);


        ArrayList<TagSchedule> ts = mDBOperator.getTagScheduleByMY(2016, 7);
        for (int i = 0; ts != null && (i < ts.size()); ++ i) {
            TagSchedule tt = ts.get(i);
            Log.e("TestTagDB",tt.getTagId() + " " + tt.getRemindId() + " " +
                    tt.getScheduleId() + " " + tt.getYear() + " " + tt.getMonth()
                    + " " + tt.getDay());
        }
        */
       // Log.e("scheduleId",scheduleId + "");
        ArrayList<Schedule> s1 = mDBOperator.getAllSchedule();
       // mDBOperator.deleteScheduleById(3);
        //Schedule s3 = new Schedule();
        //s3.setScheduleId(7);
       // s3.setContent("Hello World");
       // mDBOperator.updateSchedule(s3);
      //  mDBOperator.deleteAll();
     //   ArrayList<Schedule> s2 = mDBOperator.getAllSchedule();
        for (int i = 0; i < s1.size(); ++ i){
            Schedule ss = s1.get(i);
            Log.e("S1",ss.getScheduleId() + " " + ss.getContent() + " " + ss.getTime());
        }
        /*
        for (int i = 0; (s2 != null) && (i < s2.size()); ++ i) {
            Schedule ss = s2.get(i);
            Log.e("S2", ss.getScheduleId() + " " + ss.getContent() + " " + ss.getTime());
        }
        if (s2 == null) {
            Log.e("deleteAll", "successful");
        }
        */
    }
}
