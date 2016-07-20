package com.example.guru.pa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static SubActionButton button1;
    public static SubActionButton button2;
    public static SubActionButton button3;
    public static boolean mLoggedIn=false;

    //public final  static  String EXSTRA_MESSAGE = "com.example.guru.pa.MESSAGE";
    public void openPersonalCenter(View view){
        Intent intent = new Intent(this, LogIn.class);
        startActivity(intent);
    }

    public void createCircula() {
        // in Activity Context
        ImageView icon = new ImageView(this); // Create an icon
        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_plus));

        FloatingActionButton actionButton = new FloatingActionButton.Builder(this).setContentView(icon).build();

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);

        // repeat many times:
        ImageView itemIcon1 = new ImageView(this);
        itemIcon1.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_travel));
        button1 = itemBuilder.setContentView(itemIcon1)
                .setLayoutParams(new FloatingActionButton.LayoutParams(128,128))
                .build();

        ImageView itemIcon2 = new ImageView(this);
        itemIcon2.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_money));
        button2 = itemBuilder.setContentView(itemIcon2)
                .setLayoutParams(new FloatingActionButton.LayoutParams(128,128))
                .build();

        ImageView itemIcon3 = new ImageView(this);
        itemIcon3.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_password));
        button3 = itemBuilder.setContentView(itemIcon3)
                .setLayoutParams(new FloatingActionButton.LayoutParams(128,128))
                .build();

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(button1)
                .addSubActionView(button2)
                .addSubActionView(button3)
                .attachTo(actionButton)
                .build();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this, Activity_add_journey.class);
                startActivity(intent1);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this, AddBill.class);
                startActivity(intent2);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(MainActivity.this, AddPassword.class);
                startActivity(intent3);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createCircula();

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
                    if (!mLoggedIn) {
                        Intent intent = new Intent(MainActivity.this, LogIn.class);
                        startActivity(intent);
                    }
                    else {
                        Intent intent=new Intent(MainActivity.this,Accountcenter.class);
                        startActivity(intent);
                    }
                }
            } );
        }

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

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_settings) {
            return true;
        } else if (id == R.id.menu_search) {
            Toast.makeText(MainActivity.this, "search clicked", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_travel) {
            Intent intent = new Intent(this, JourneyManage.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_money) {
            Intent intent = new Intent(this, MoneyManage.class);
            startActivity(intent);
        } else if (id == R.id.nav_password) {
            Intent intent = new Intent(this, Password.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
