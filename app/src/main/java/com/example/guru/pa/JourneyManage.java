package com.example.guru.pa;

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

    }

    public void openJourneySearch(){

    }

    public void openJourneySort(){

    }
}
