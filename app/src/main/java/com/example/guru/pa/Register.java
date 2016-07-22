package com.example.guru.pa;

import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Register extends AppCompatActivity {
    private EditText reg_username_edit;
    private EditText reg_password1_edit;
    private EditText reg_password2_edit;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

        reg_username_edit = (EditText)findViewById(R.id.reg_username_edit);
        reg_password1_edit = (EditText)findViewById(R.id.reg_password1_edit);
        reg_password2_edit = (EditText)findViewById(R.id.reg_password2_edit);
        button = (Button)findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = reg_username_edit.getText().toString();
                String password = reg_password1_edit.getText().toString();
                String password1 = reg_password2_edit.getText().toString();

                if (name.equals("") || password.equals("")||password1.equals("")){
                    Toast.makeText(Register.this,"请将帐号密码填完整", Toast.LENGTH_SHORT).show();
                }else if(!password.equals(password1)){
                    Toast.makeText(Register.this,"两次输入密码不一致", Toast.LENGTH_SHORT).show();
                }else {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    RequestParams requestParams = new RequestParams();
                    requestParams.add("username",name);
                    requestParams.add("password",password);
                    onReg("regist/",requestParams);
                }
            }
        });

    }

    /**
     * 注册
     * */
    public void onReg(String url, RequestParams params) {
        HttpClient.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                String status = null;
                String res = "11";

                try {
                    status = response.getString("status");
                    res = response.getString("response");
                } catch (JSONException e) {
                    Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                switch (status) {
                    case "11000":
                        ActivityController.jumpToAnotherActivity(Register.this, LogIn.class);
                        break;
                    default:
                        break;
                }

                Toast.makeText(Register.this, status+","+res, Toast.LENGTH_SHORT).show();
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
        switch (id) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}