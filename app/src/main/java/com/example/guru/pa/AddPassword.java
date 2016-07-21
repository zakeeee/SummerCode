package com.example.guru.pa;

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

public class AddPassword extends AppCompatActivity {
    private SQLiteDatabase db;
    private EditText addpassword_purpose;//用途
    private EditText addpassword_username;//账号
    private EditText addpassword_password;//密码
    private EditText addpassword_extra;//备注
    RadioGroup radiogroup;
    Button save;
    Button cancel;
    public static int check = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_password);

        //打开或创建数据库/data/data/com.example.guru.pa/files
        db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString()+"/password.dbs", null);

        //ActionBar添加返回按钮
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //通过findViewById获得对象
        radiogroup = (RadioGroup)findViewById(R.id.addpassword_radiogroup);
        save = (Button)findViewById(R.id.addpassword_save);
        cancel = (Button)findViewById(R.id.addpassword_cancel);
        addpassword_purpose = (EditText)findViewById(R.id.addpassword_purpose);
        addpassword_username = (EditText)findViewById(R.id.addpassword_username);
        addpassword_password = (EditText)findViewById(R.id.addpassword_password);
        addpassword_extra = (EditText)findViewById(R.id.addpassword_extra);

        //添加事件监听器
        radiogroup.setOnCheckedChangeListener(new RadioGroupListener());
        save.setOnClickListener(new ButtonListener());
        cancel.setOnClickListener(new ButtonListener());
    }
    //监听按钮按下
    private class ButtonListener implements View.OnClickListener{
        public void onClick (View v){
            if(v.getId()==R.id.addpassword_save){
                String purpose = addpassword_purpose.getText().toString();
                String username = addpassword_username.getText().toString();
                String password = addpassword_password.getText().toString();
                String extra = addpassword_extra.getText().toString();
                byte[] encode = DES3Utils.encryptMode(password.getBytes());//加密测试
                byte[] decode = DES3Utils.decryptMode(encode);//解密
                if(check==1){
                    //  Toast.makeText(AddPassword.this, "本地hahah", Toast.LENGTH_SHORT).show();
                    //本地备份
                    Toast.makeText(AddPassword.this, "加密后"+new String(encode), Toast.LENGTH_SHORT).show();
                    Toast.makeText(AddPassword.this,"解密"+ new String(decode), Toast.LENGTH_SHORT).show();
                    //LocalSave(purpose, username, password, extra);
                    // Toast.makeText(AddPassword.this, "保存成功！", Toast.LENGTH_SHORT).show();
                }else if(check ==2){
                    Toast.makeText(AddPassword.this, "云端还木有实现功能~~", Toast.LENGTH_SHORT).show();
                    //云端备份
                    Toast.makeText(AddPassword.this, "保存功能还没写呢", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(AddPassword.this, "请选择保存方式！~~", Toast.LENGTH_SHORT).show();
                }
            }
            if(v.getId()==R.id.addpassword_cancel)
                Toast.makeText(AddPassword.this, "已取消", Toast.LENGTH_SHORT).show();
        }
    }
    //本地保存
    private void LocalSave(String purpose, String username, String password, String extra){
        String str = "insert into add_password values(?,?,?,?) ";
        try{
            db.execSQL(str, new String[]{purpose, username, password, extra});

        }catch(Exception e)    {
            String sql="create table add_password(purpose varchar(50), username varchar(50), password varchar(50), extra varchar(50) )";
            db.execSQL(sql);
        }
    }
    //监听保存方式
    private class RadioGroupListener implements RadioGroup.OnCheckedChangeListener{
        public void onCheckedChanged(RadioGroup group, int checkedId){
            if(checkedId ==  R.id.addpassword_radio_local){
                check=1;
            }else if(checkedId ==  R.id.addpassword_radio_cloud){
                check=2;
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
        check = 0;
    }
}
