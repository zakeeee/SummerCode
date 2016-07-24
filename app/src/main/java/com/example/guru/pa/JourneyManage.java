package com.example.guru.pa;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JourneyManage extends AppCompatActivity {

    /* 必备的三个量：一个List（也可以为数组）,一个Adapter,一个ListView */
    private ArrayList<String> strs;
    private ArrayList<Integer> mHash;
    private ArrayAdapter<String> arrayAdapter;
    private SwipeMenuListView mListView;
    private ArrayList<Schedule> mScheduleArrayList;
    private List<Schedule> mNewList;
    //private ArrayList<TagSchedule> mTagScheduleArrayList;
    private DataBaseOperator mDBOperator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_manage);

        /* ActionBar添加返回按钮 */
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        /* 实例化SwipeMenuListView */
        mListView = (SwipeMenuListView) findViewById(R.id.journey_list);

        /* 设置mListView的View最小高度 */
        mListView.setMinimumHeight(180);

        /* 实例化SwipeMenuCreator */
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                /* create "open" item */
                SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                openItem.setWidth(180);
                openItem.setTitle("Open");
                openItem.setTitleSize(18);
                openItem.setTitleColor(Color.rgb(0x00, 0x00, 0x00));
                // 添加到SwipeMenu
                menu.addMenuItem(openItem);

                /* create "delete" item */
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                deleteItem.setWidth(180);
                deleteItem.setTitle("X"); /* 未来会换成icon */
                deleteItem.setTitleSize(18);
                deleteItem.setTitleColor(Color.WHITE);
                // 添加到SwipeMenu
                menu.addMenuItem(deleteItem);
            }
        };

        /* 给mListView设置Adapter,MenuCreator,设置滑动方向 */

        mListView.setMenuCreator(creator);
        mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        /* 给mListView添加按钮点击监听 */
        mListView.setOnMenuItemClickListener(   new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                        sendId(position);
                        break;
                    case 1:
                        strs.remove(position);
                        deleteContent(position);
                        arrayAdapter.notifyDataSetChanged();
                        // delete
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mHash = new ArrayList<Integer>();
        strs = new ArrayList<String>();
        mScheduleArrayList = new ArrayList<Schedule>();
        //mTagScheduleArrayList = new ArrayList<TagSchedule>();
        mDBOperator = new DataBaseOperator(this);
        mScheduleArrayList = mDBOperator.getAllSchedule();

        displayContent(mScheduleArrayList);

        /* 实例化ArrayAdapter */
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strs);

        mListView.setAdapter(arrayAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_journey, menu);

         /* 设置菜单项的搜索项 */
        MenuItem searchItem = menu.findItem(R.id.journey_search);

        /* 给搜索项添加展开和缩起监听器 */
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }
            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                return true;
            }
        });

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

    public void deleteContent(int position) {
        int deleteId = mHash.get(position);
        mHash.remove(position);
        mDBOperator.deleteScheduleById(deleteId);
        mScheduleArrayList.remove(position);
    }

    public void sendId(int position) {
        int sendId = mHash.get(position);
        Intent  intent = new Intent(this, JourneyDetail.class);
        intent.putExtra("pa.journey.manage.detail", sendId);

        startActivity(intent);
    }

    public void openJourneyAdd(){
        Intent intent = new Intent(JourneyManage.this, AddJourney.class);
        startActivity(intent);
    }

    public void openJourneySearch(){

    }

    public void openJourneySort(){
        strs.clear();
        arrayAdapter.notifyDataSetChanged();
        mHash.clear();
        mNewList = new ArrayList<Schedule>();
        mNewList = mScheduleArrayList;
        Collections.sort(mNewList);
        displayContent(new ArrayList<Schedule>(mNewList));
    }

    public void displayContent(ArrayList<Schedule> list) {
        if(list == null){
            Toast.makeText(JourneyManage.this, "无内容", Toast.LENGTH_SHORT).show();
            strs.add("木有内容");
        }
        else {
            String tempStr = "";
            String tempContent = "";
            Schedule tempSch = null;
            for (int i = 0; i < list.size(); ++ i) {
                tempSch = list.get(i);
                tempStr = "Date: " + tempSch.getDate() + " " +
                          "Time: " + tempSch.getTime() + "\n";

                tempContent = tempSch.getContent();

                int maxLen = 20;
                if (tempContent.length() > maxLen) {
                    tempContent = "Content :" + tempContent.substring(0, maxLen - 1);
                }
                else {
                    tempContent = "Content :" + tempContent;
                }
                mHash.add(tempSch.getScheduleId());
                strs.add(tempStr + tempContent);
            }
        }
    }
}
