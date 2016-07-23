package com.example.guru.pa;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class AddPassword extends AppCompatActivity {
    private EditText addpassword_purpose;//用途
    private EditText addpassword_username;//账号
    private EditText addpassword_password;//密码
    private EditText addpassword_extra;//备注
    private PasswordOperate mpasswordOperate;
    RadioGroup radiogroup;
    public static int check = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_password);

        //ActionBar添加返回按钮
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //实例化
        radiogroup = (RadioGroup)findViewById(R.id.addpassword_radiogroup);
        addpassword_purpose = (EditText)findViewById(R.id.addpassword_purpose);
        addpassword_username = (EditText)findViewById(R.id.addpassword_username);
        addpassword_password = (EditText)findViewById(R.id.addpassword_password);
        addpassword_extra = (EditText)findViewById(R.id.addpassword_extra);

        //添加事件监听器
        radiogroup.setOnCheckedChangeListener(new RadioGroupListener());

        if(PasswordManage.update_PM_id != 0){
            mpasswordOperate = new PasswordOperate(this);
            PasswordMessage tempPM = mpasswordOperate.getByid(PasswordManage.update_PM_id);
            //解密
            String de_purpose = DES3Utils.decryptMode(tempPM.getPurpose());
            String de_username = DES3Utils.decryptMode(tempPM.getUsername());
            String de_password = DES3Utils.decryptMode(tempPM.getPassword());
            String de_extra = DES3Utils.decryptMode(tempPM.getExtra());
            addpassword_purpose.setText(de_purpose);
            addpassword_username.setText(de_username);
            addpassword_password.setText(de_password);
            addpassword_extra.setText(de_extra);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home) {
            AddPassword.this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //监听保存方式
    private class RadioGroupListener implements RadioGroup.OnCheckedChangeListener{
        public void onCheckedChanged(RadioGroup group, int checkedId){
            if(checkedId ==  R.id.addpassword_radio_cloud)
                check=1;
            else if(checkedId ==  R.id.addpassword_radio_local)
                check=0;
        }
    }
    //保存
    public void savePassword(View view){
        String purpose = addpassword_purpose.getText().toString();
        String username = addpassword_username.getText().toString();
        String password = addpassword_password.getText().toString();
        String extra = addpassword_extra.getText().toString();

        if(username.equals("")||password.equals("")){
            Toast.makeText(AddPassword.this, "账号密码不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        //数据加密
        String en_purpose = DES3Utils.encryptMode(purpose);
        String en_username = DES3Utils.encryptMode(username);
        String en_password = DES3Utils.encryptMode(password);
        String en_extra = DES3Utils.encryptMode(extra);

        if(PasswordManage.update_PM_id != 0){/***修改信息***/
            if(check==0){
                //修改本地备份
                mpasswordOperate = new PasswordOperate(this);
                PasswordMessage PM = new PasswordMessage(PasswordManage.update_PM_id,en_purpose,en_username,en_password,en_extra);
                mpasswordOperate.update(PM);

                if (mpasswordOperate != null){
                    mpasswordOperate.closeDB();
                }
                Intent intent = new Intent(this, PasswordManage.class);
                startActivity(intent);
                AddPassword.this.finish();
            }else if(check ==1){
                Toast.makeText(AddPassword.this, "云端还木有实现功能~~", Toast.LENGTH_SHORT).show();
                //修改云端备份
            }
        }
        else {/***增加新信息***/
            if(check==0){
                //本地备份
                PasswordMessage PM = new PasswordMessage();
                mpasswordOperate = new PasswordOperate(this);

                PM.setPurpose(en_purpose);
                PM.setUsername(en_username);
                PM.setPassword(en_password);
                PM.setExtra(en_extra);

                mpasswordOperate.save(PM);
                if (mpasswordOperate != null){
                    mpasswordOperate.closeDB();
                }
                Intent intent = new Intent(this, PasswordManage.class);
                startActivity(intent);
                AddPassword.this.finish();
            }else if(check ==1){
                Toast.makeText(AddPassword.this, "云端还木有实现功能~~", Toast.LENGTH_SHORT).show();
                //云端备份
            }
        }
    }

    //取消
    public void cancelPassword(View view){
        if (mpasswordOperate != null){
            mpasswordOperate.closeDB();
        }
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        check = 0;
        PasswordManage.update_PM_id = 0;
    }

    /* 添加到云端 */
    private void addToCloud(RequestParams params) {

        /* 发送到的url */
        String url = "postpassword/";

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
                        case "20000":
                            AddPassword.this.onBackPressed();
                            break;
                        default:
                            break;
                    }
                } catch (JSONException e) {
                    Toast.makeText(AddPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                /* 提示返回信息 */
                Toast.makeText(AddPassword.this, res, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                /* 超时提示 */
                Toast.makeText(AddPassword.this, "连接超时，请检查网络连接", Toast.LENGTH_SHORT).show();
            }
        });
    }
}