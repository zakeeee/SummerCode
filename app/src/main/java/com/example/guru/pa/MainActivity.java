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

import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String FILENAME = "testFile.txt";

    private ResideMenu mResideMenu;
    public static Boolean LOGGEDIN = false;
    public static String USERNAME;
    private ResideMenuItem mItem[];

    /**/
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavigationView;
    private View mNavHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //createResideMenu();

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(mToggle);
        mToggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        mNavHeader = mNavigationView.getHeaderView(0);

    }

    @Override
    protected void onResume() {
        super.onResume();

        mNavHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNavHeaderClick();
            }
        });

        if(MainActivity.LOGGEDIN){
            TextView tv = (TextView) mNavHeader.findViewById(R.id.logged_username);
            tv.setText(USERNAME);
        }
    }

    /**
     * 创建右边划出菜单
     * */
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
        mItem = new ResideMenuItem[titles.length];

        for (int i = 0; i < titles.length; i++){
            mItem[i] = new ResideMenuItem(this, icon[i], titles[i]);
            mResideMenu.addMenuItem(mItem[i],  ResideMenu.DIRECTION_RIGHT); // or  ResideMenu.DIRECTION_RIGHT
        }

        mItem[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityController.jumpToAnotherActivity(MainActivity.this, AddJourney.class);
            }
        });

        mItem[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityController.jumpToAnotherActivity(MainActivity.this, AddBill.class);
            }
        });

        mItem[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityController.jumpToAnotherActivity(MainActivity.this, AddPassword.class);
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
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_plus:
                if(mResideMenu.isOpened()) {
                    mResideMenu.closeMenu();
                } else {
                    mResideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
                }
                return true;
            default:
                break;
        }

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
}
