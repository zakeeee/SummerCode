package com.example.guru.pa;

import android.app.Dialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;

import cz.msebera.android.httpclient.Header;

public class Bill extends AppCompatActivity {

    private BillDBOperator mDBOperator = null;
    private ArrayList<BillVO> mBillList = null;
    private ArrayList<Integer> mHash = null;
    /* 必备的三个量：一个List（也可以为数组）,一个Adapter,一个ListView */
    private ArrayList<String> strs;
    private ArrayAdapter<String> arrayAdapter;
    private SwipeMenuListView mListView;
    public static final String SEND_TAG = "sendId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);



        /* 实例化SwipeMenuListView */
        mListView = (SwipeMenuListView) findViewById(R.id.bill_list);

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
                        Intent intent = new Intent(Bill.this, MoneyDetail.class);
                        startActivity(intent);
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

        getFromCloud(User.userDownloadBill());

        strs = new ArrayList<String>();
        mHash = new ArrayList<Integer>();
        mBillList = new ArrayList<BillVO>();
        mDBOperator = new BillDBOperator(this);
        mBillList = mDBOperator.getAllBill();
        if(mBillList == null){
            Toast.makeText(Bill.this, "无内容", Toast.LENGTH_SHORT).show();
            strs.add("木有内容");
        }
        else {
            BillVO billVO = null;
            String lineContent = "";
            for (int i = 0; i <  mBillList.size(); ++ i) {
                billVO = mBillList.get(i);
                lineContent = "billId: " + billVO.getBillId() + " " +
                        "date: " + billVO.getYear() + "-" + billVO.getMonth() + "-" + billVO.getDay() + "\n" +
                        "支出: " + billVO.getExpend() + " " + "收入: " + billVO.getIncome();
                mHash.add(billVO.getBillId());
                strs.add(lineContent);
            }
        }

        /* 实例化ArrayAdapter */
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strs);

        mListView.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_bill,menu);

       /* SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        ComponentName cn = new ComponentName(this, SearchableActivity.class);
        SearchableInfo info = searchManager.getSearchableInfo(cn);

        searchView.setSearchableInfo(info);*/

        /* 设置菜单项的搜索项 */
        MenuItem searchItem = menu.findItem(R.id.bill_search);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.bill_plus:
                Intent intent = new Intent(Bill.this, AddBill.class);
                startActivity(intent);
                break;
            case android.R.id.home:
                this.finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void sendId(int position) {
        int sendId = mHash.get(position);
        Intent  intent = new Intent(this, MoneyDetail.class);
        intent.putExtra(SEND_TAG, sendId);
        startActivity(intent);
    }

    public void deleteContent(int index){
        int billId = mHash.get(index);
        mHash.remove(index);
        mDBOperator.deleteBillById(billId);
    }

    /* 从云端获取 */
    private void getFromCloud(RequestParams params) {

        /* 发送到的url */
        String url = "getbill/";

        /* POST请求 */
        HttpClient.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                String status = null;
                String res = "11";

                try {
                    status = response.getString("status");
                    res = response.getString("response");

                    /* 判断返回码 */
                    switch(status) {
                        case "30000":
                            JSONObject list = response.getJSONObject("list");

                            for(int i = 0; i < list.length(); i++) {
                                JSONObject ith = new JSONObject(list.getString(String.valueOf(i)));

                                String date = ith.getString("date");
                                String income = ith.getString("income");
                                String incomefrom = ith.getString("incomefrom");
                                String expand = ith.getString("expand");
                                String expandto = ith.getString("expandto");
                                String extra = ith.getString("extra");

                                strs.add("日期："+date+"\n"
                                        +"收入："+income+"\n"
                                        +"收入来源："+incomefrom+"\n"
                                        +"支出："+expand+"\n"
                                        +"支出目的："+expandto+"\n"
                                        +"备注："+extra+"\n"
                                        +"<云端存储>"
                                );
                            }
                            arrayAdapter.notifyDataSetChanged();
                            break;
                        default:
                            break;
                    }
                } catch (JSONException e) {
                    Toast.makeText(Bill.this, "exception", Toast.LENGTH_SHORT).show();
                }

                /* 提示返回信息 */
                Toast.makeText(Bill.this, res, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                /* 超时提示 */
                Toast.makeText(Bill.this, "连接超时，请检查网络连接", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
