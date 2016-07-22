package com.example.guru.pa;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.database.sqlite.SQLiteDatabase;
import android.app.AlertDialog;
import android.database.sqlite.SQLiteException;
import android.database.Cursor;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import cz.msebera.android.httpclient.Header;

public class LogIn extends AppCompatActivity {
    private EditText edname;
    private EditText edpassword;
    private Button login;
    private String name;
    public static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /* 添加返回按钮 */
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        edname = (EditText) findViewById(R.id.editText);
        edpassword = (EditText) findViewById(R.id.editText2);
        login = (Button) findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = edname.getText().toString();
                String password = edpassword.getText().toString();

                if (name.equals("") || password.equals("")){
                    Toast.makeText(LogIn.this,"用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    RequestParams requestParams = new RequestParams();
                    requestParams.add("username",name);
                    requestParams.add("password",password);
                    onLogIn("login/",requestParams);

                }

            }
        });

        /* 注册按钮 */
        TextView regist = (TextView) findViewById(R.id.regist);
        if(regist != null){
            regist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LogIn.this, Register.class);
                    startActivity(intent);
                }
            });
        }

    }

    /**
     * 登陆
     * */
    public void onLogIn(String url, RequestParams params) {
        HttpClient.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                String status = null;
                String res = "11";

                try {
                    status = response.getString("status");
                    res = response.getString("response");
                } catch (JSONException e) {
                    Toast.makeText(LogIn.this, "jsonerror", Toast.LENGTH_SHORT).show();
                    return;
                }

                switch (status) {
                    case "10000":
                        MainActivity.LOGGEDIN = true;
                        MainActivity.USERNAME = name;
                        ActivityController.jumpToAnotherActivity(LogIn.this, MainActivity.class);
                        break;
                    default:
                        break;
                }

                Toast.makeText(LogIn.this, status+","+res, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home) {
            Intent intent = new Intent(LogIn.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
