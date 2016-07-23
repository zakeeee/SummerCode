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
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class PasswordManage extends AppCompatActivity {

    /* 必备的三个量：一个List（也可以为数组）,一个Adapter,一个ListView */
    private ArrayList<String> strs;
    private ArrayAdapter<String> arrayAdapter;
    private SwipeMenuListView mListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        /* ActionBar添加返回按钮 */
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        /* 实例化SwipeMenuListView */
        mListView = (SwipeMenuListView) findViewById(R.id.password_list);

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
                        Intent intent = new Intent(PasswordManage.this,PasswordDetail.class);
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
        /**
         * 强烈不建议在onCreate里面进行以下操作
         * 数据量大时会让人感觉界面卡顿
         * 建议在另一个线程里加载，然后更新UI
         */


        strs = new ArrayList<String>();

       // if(fileContent == null){
            Toast.makeText(PasswordManage.this, "打开文件失败", Toast.LENGTH_SHORT).show();
            strs.add("木有内容");
        /*
        }
        else {

        }*/

        getFromCloud(User.userDownloadPassword());


        /* 实例化ArrayAdapter */
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strs);

        mListView.setAdapter(arrayAdapter);


    }


    public void deleteContent(int index){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_password, menu);

         /* 设置菜单项的搜索项 */
        MenuItem searchItem = menu.findItem(R.id.password_search);

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
            case R.id.password_plus:
                Intent intent = new Intent(PasswordManage.this, AddPassword.class);
                startActivity(intent);
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /* 从云端获取 */
    private void getFromCloud(RequestParams params) {

        /* 发送到的url */
        String url = "getpassword/";

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

                                String purpose = ith.getString("purpose");
                                String uname = ith.getString("uname");
                                String passwd = ith.getString("passwd");
                                String extra = ith.getString("extra");

                                strs.add("目的："+purpose+"\n"
                                        +"账号："+uname+"\n"
                                        +"密码："+passwd+"\n"
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
                    Toast.makeText(PasswordManage.this, "exception", Toast.LENGTH_SHORT).show();
                }

                /* 提示返回信息 */
                Toast.makeText(PasswordManage.this, res, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                /* 超时提示 */
                Toast.makeText(PasswordManage.this, "连接超时，请检查网络连接", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* 云端删除 */
    private void deleteFromCloud(RequestParams params) {

        /* 发送到的url */
        String url = "deletepassword/";

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

                                String purpose = ith.getString("purpose");
                                String uname = ith.getString("uname");
                                String passwd = ith.getString("passwd");
                                String extra = ith.getString("extra");

                                strs.add("目的："+purpose+"\n"
                                        +"账号："+uname+"\n"
                                        +"密码："+passwd+"\n"
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
                    Toast.makeText(PasswordManage.this, "exception", Toast.LENGTH_SHORT).show();
                }

                /* 提示返回信息 */
                Toast.makeText(PasswordManage.this, res, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                /* 超时提示 */
                Toast.makeText(PasswordManage.this, "连接超时，请检查网络连接", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
