package com.example.guru.pa;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //public static final String FILENAME = "testFile2.txt";
    public static final String MESSAGE_JOURNEY = "pa.scheduleId";
    public static final String MESSAGE_BILL = "pa.billId";
    public static SubActionButton button1;
    public static SubActionButton button2;
    public static SubActionButton button3;
    private  ResideMenu mResideMenu;
    public static Boolean LOGGEDIN = false;
    public static String USERNAME;
    private ResideMenuItem item[];
    private DataBaseOperator mJourneyDB;
    private ArrayList<TagSchedule> mJourneyList;


    private void createResideMenu() {
        // attach to current activity;
        mResideMenu = new ResideMenu(this);
        mResideMenu.setBackground(R.drawable.menu_background);
        mResideMenu.attachToActivity(this);
        mResideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);
        mResideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

        // create menu items;
        String titles[] = { "添加行程", "添加账单", "添加密码" };
        int icon[] = { R.drawable.ic_menu_travel, R.drawable.ic_menu_money, R.drawable.ic_menu_password };
        item = new ResideMenuItem[titles.length];

        for (int i = 0; i < titles.length; i++){
            item[i] = new ResideMenuItem(this, icon[i], titles[i]);
            mResideMenu.addMenuItem(item[i],  ResideMenu.DIRECTION_RIGHT); // or  ResideMenu.DIRECTION_RIGHT
        }

        item[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddJourney.class);
                startActivity(intent);
            }
        });

        item[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddBill.class);
                startActivity(intent);
            }
        });

        item[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddPassword.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initCard();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createResideMenu();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View cir = navigationView.getHeaderView(0);
        if (cir != null) {
            cir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    if(MainActivity.LOGGEDIN) {
                        Intent intent = new Intent(MainActivity.this, AccountCenter.class);
                        startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(MainActivity.this, LogIn.class);
                        startActivity(intent);
                    }
                }
            } );
        }

        if(MainActivity.LOGGEDIN){
            TextView tv = (TextView) cir.findViewById(R.id.logged_username);
            tv.setText(USERNAME);
        }

    }

    /**
     * 初始化卡片
     */
    public void initCard() {
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        String year = yearFormat.format(new Date());
        String month = monthFormat.format(new Date());
        mJourneyDB = new DataBaseOperator(this);
        mJourneyList = mJourneyDB.getTagScheduleByMY(Integer.parseInt(year), Integer.parseInt(month));

        TextView card1 = (TextView)findViewById(R.id.card1);
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mJourneyList != null)
                    goToJourneyDetail(mJourneyList.get(0).getScheduleId());
            }
        });

        TextView card2 =(TextView)findViewById(R.id.card2);
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mJourneyList != null && mJourneyList.size() > 1)
                    goToJourneyDetail(mJourneyList.get(1).getScheduleId());
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        /* 设置菜单项的搜索项 */
        MenuItem searchItem = menu.findItem(R.id.menu_search);

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

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        switch (id){
            case R.id.menu_plus:
                if(mResideMenu.isOpened()) {
                    mResideMenu.closeMenu();
                } else {
                    mResideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
                }
                break;
            case R.id.menu_search:
                Toast.makeText(MainActivity.this, "search clicked", Toast.LENGTH_SHORT).show();
                break;
//            case R.id.item1:
//                Toast.makeText(MainActivity.this, "111", Toast.LENGTH_SHORT).show();
//                Intent intent=new Intent(MainActivity.this,AddJourney.class);
//                startActivity(intent);
//                break;
            default:
                break;
        }
        //noinspection SimplifiableIfStatement
//        if (id == R.id.menu_plus) {
//            if(mResideMenu.isOpened()) {
//                mResideMenu.closeMenu();
//            } else {
//                mResideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
//            }
//            return true;
//        } else if (id == R.id.menu_search) {
//            Toast.makeText(MainActivity.this, "search clicked", Toast.LENGTH_SHORT).show();
//        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_travel:
                ActivityController.jumpToAnotherActivity(MainActivity.this, JourneyManage.class);
                return true;
            case R.id.nav_money:
                ActivityController.jumpToAnotherActivity(MainActivity.this, MoneyManage.class);
                return true;
            case R.id.nav_password:
                ActivityController.jumpToAnotherActivity(MainActivity.this, PasswordManage.class);
                return true;
            case R.id.nav_settings:
                ActivityController.jumpToAnotherActivity(MainActivity.this, SettingsActivity.class);
                return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void onNavHeaderClick() {
        if(MainActivity.LOGGEDIN) {
            ActivityController.jumpToAnotherActivity(MainActivity.this, AccountCenter.class);
        } else {
            ActivityController.jumpToAnotherActivity(MainActivity.this, LogIn.class);
        }
    }


    public void goToJourneyDetail(int sendId) {

        Intent intent=new Intent(MainActivity.this,JourneyDetail.class);
        intent.putExtra(MainActivity.MESSAGE_JOURNEY, sendId);
        startActivity(intent);
    }
}
