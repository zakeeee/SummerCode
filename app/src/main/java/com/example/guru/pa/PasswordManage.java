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
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import java.lang.String;

public class PasswordManage extends AppCompatActivity {
    public static Integer update_PM_id = 0;
    private PasswordOperate mPasswordOperate;
    private ArrayList<PasswordMessage> mPMArrayList;
    private ArrayList<Integer> mhash = null;
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

        /**
         * 强烈不建议在onCreate里面进行以下操作
         * 数据量大时会让人感觉界面卡顿
         * 建议在另一个线程里加载，然后更新UI
         */

        strs = new ArrayList<String>();
        mPMArrayList = new ArrayList<PasswordMessage>();
        mhash = new ArrayList<Integer>();
        mPasswordOperate = new PasswordOperate(this);
        mPMArrayList = mPasswordOperate.getAllPasswordMessage();
        if(mPMArrayList == null){
            Toast.makeText(PasswordManage.this, "无内容", Toast.LENGTH_SHORT).show();
            strs.add("木有内容");
        }
        else {
            String tempStr = "";
            PasswordMessage tempPM = null;
            for (int i = 0; i < mPMArrayList.size(); i++) {
                tempPM = mPMArrayList.get(i);
                //解密
                String de_purpose = DES3Utils.decryptMode(tempPM.getPurpose());
                String de_username = DES3Utils.decryptMode(tempPM.getUsername());
                String de_password = DES3Utils.decryptMode(tempPM.getPassword());
                String de_extra = DES3Utils.decryptMode(tempPM.getExtra());

                tempStr ="用途:" + de_purpose + "\n" +
                        "账号:" + de_username + "\n"+
                        "密码:" + de_password + "\n"+
                        "备注:" + de_extra;
                mhash.add(tempPM.getId());
                strs.add(tempStr);
            }
        }

        /* 实例化ArrayAdapter */
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strs);

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
                openItem.setTitle("编辑");
                openItem.setTitleSize(18);
                openItem.setTitleColor(Color.rgb(0x00, 0x00, 0x00));
                // 添加到SwipeMenu
                menu.addMenuItem(openItem);

                /* create "delete" item */
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                deleteItem.setWidth(180);
                deleteItem.setTitle("删除"); /* 未来会换成icon */
                deleteItem.setTitleSize(18);
                deleteItem.setTitleColor(Color.WHITE);
                // 添加到SwipeMenu
                menu.addMenuItem(deleteItem);
            }
        };

        /* 给mListView设置Adapter,MenuCreator,设置滑动方向 */
        mListView.setAdapter(arrayAdapter);
        mListView.setMenuCreator(creator);
        mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        /* 给mListView添加按钮点击监听 */
        mListView.setOnMenuItemClickListener(   new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // 编辑
                        update_PM_id = mhash.get(position);
                        Intent intent = new Intent(PasswordManage.this, AddPassword.class);
                        startActivity(intent);
                        PasswordManage.this.finish();
                        break;
                    case 1:
                        // 删除
                        strs.remove(position);
                        deleteContent(position);
                        arrayAdapter.notifyDataSetChanged();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }


    public void deleteContent(int position){
        int id = mhash.get(position);
        mhash.remove(position);
        mPasswordOperate.deleteByid(id);
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
                PasswordManage.this.finish();
                return true;
            case android.R.id.home:
                PasswordManage.this.finish();
                return true;
            case R.id.password_search:
                Toast.makeText(PasswordManage.this, "暂时木有功能！", Toast.LENGTH_SHORT).show();
                break;
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

                                Integer id = ith.getInt("id");
                                String purpose = ith.getString("purpose");
                                String uname = ith.getString("uname");
                                String passwd = ith.getString("passwd");
                                String extra = ith.getString("extra");

                                String content = "目的："+purpose+"\n"
                                            +"账号："+uname+"\n"
                                            +"密码："+passwd+"\n"
                                            +"备注："+extra+"\n"
                                            +"<云端存储>";

                                Info info = new Info(id, false, content);
                                Log.e("id", String.valueOf(id));
                                Log.e("content", content);

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
    private void deleteFromCloud(RequestParams params, final int position) {

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
                        case "40000":
                            //mInfo.remove(position);
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
