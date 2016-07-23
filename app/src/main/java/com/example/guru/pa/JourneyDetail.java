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

public class JourneyDetail extends AppCompatActivity {

    private DataBaseOperator mDBOperator;
    private int scheduleId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (intent != null) {
            scheduleId = intent.getIntExtra(MainActivity.MESSAGE_JOURNEY, 0);

            mDBOperator = new DataBaseOperator(this);
            Schedule schedule = mDBOperator.getScheduleById(scheduleId);
            TextView date = (TextView)findViewById(R.id.journey_detail_date);
            TextView time = (TextView)findViewById(R.id.journey_detail_time);
            TextView backup = (TextView)findViewById(R.id.journey_detail_backup);

            date.setText("日期：\n" + schedule.getDate());
            time.setText("时间：\n" + schedule.getTime());
            backup.setText("内容：\n" + schedule.getContent());
        }
    }

    @Override
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

    public void sendId() {
        Intent intent = new Intent(JourneyDetail.this, AddJourney.class);
        intent.putExtra(MainActivity.MESSAGE_JOURNEY, scheduleId);
        startActivity(intent);
    }
}
