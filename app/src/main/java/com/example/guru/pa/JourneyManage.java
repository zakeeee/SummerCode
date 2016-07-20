package com.example.guru.pa;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class JourneyManage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_manage);

        /* ActionBar添加返回按钮 */
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_journey, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.journey_plus:
                openJourneyAdd();
                return true;
            case R.id.journey_search:
                openJourneySearch();
                return true;
            case R.id.journey_sort:
                openJourneySort();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openJourneyAdd(){
        Intent intent = new Intent(JourneyManage.this, Activity_add_journey.class);
        startActivity(intent);
    }

    public void openJourneySearch(){

    }

    public void openJourneySort(){

    }
}
