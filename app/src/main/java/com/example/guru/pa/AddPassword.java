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

public class AddPassword extends AppCompatActivity {
  //  private SQLiteDatabase db;
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

        //打开或创建数据库/data/data/com.example.guru.pa/files
     //   db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString()+"/password.dbs", null);

        //ActionBar添加返回按钮
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //通过findViewById获得对象
        radiogroup = (RadioGroup)findViewById(R.id.addpassword_radiogroup);
        //添加事件监听器
        radiogroup.setOnCheckedChangeListener(new RadioGroupListener());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home) {
            this.finish();
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
    /*//本地保存
    private void LocalSave(String purpose, String username, String password, String extra){
        String str = "insert into add_password values(?,?,?,?) ";
        try{
            db.execSQL(str, new String[]{purpose, username, password, extra});

        }catch(Exception e)    {
            String sql="create table add_password(purpose varchar(50), username varchar(50), password varchar(50), extra varchar(50) )";
            db.execSQL(sql);
        }
    }*/
    //保存
    public void savePassword(View view){
        addpassword_purpose = (EditText)findViewById(R.id.addpassword_purpose);
        addpassword_username = (EditText)findViewById(R.id.addpassword_username);
        addpassword_password = (EditText)findViewById(R.id.addpassword_password);
        addpassword_extra = (EditText)findViewById(R.id.addpassword_extra);

        String purpose = addpassword_purpose.getText().toString();
        String username = addpassword_username.getText().toString();
        String password = addpassword_password.getText().toString();
        String extra = addpassword_extra.getText().toString();

        if(username.equals("")||password.equals("")){
            Toast.makeText(AddPassword.this, "账号密码不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        //数据加密
        byte[] en_purpose = DES3Utils.encryptMode(purpose.getBytes());
        byte[] en_username = DES3Utils.encryptMode(username.getBytes());
        byte[] en_password = DES3Utils.encryptMode(password.getBytes());
        byte[] en_extra = DES3Utils.encryptMode(extra.getBytes());

        String S_en_purpose = new String(en_purpose);
        String S_en_username = new String(en_username);
        String S_en_password = new String(en_password);
        String S_en_extra = new String(en_extra);

        // byte[] de_username = DES3Utils.decryptMode(en_username);//解密
        if(check==0){
            //本地备份
            PasswordMessage passwordMessage = new PasswordMessage();
            mpasswordOperate = new PasswordOperate(this);
            passwordMessage.setPurpose(S_en_purpose);
            passwordMessage.setUsername(S_en_username);
            passwordMessage.setPassword(S_en_password);
            passwordMessage.setExtra(S_en_extra);
            mpasswordOperate.save(passwordMessage);
          //  Toast.makeText(AddPassword.this, "加密:"+S_en_purpose, Toast.LENGTH_SHORT).show();
          //  Toast.makeText(AddPassword.this,"解密:"+ new String(de_username), Toast.LENGTH_SHORT).show();
            //LocalSave(en_purpose,en_username,en_password,en_extra);
            Toast.makeText(AddPassword.this, "保存成功！", Toast.LENGTH_SHORT).show();
            //销毁当前activity
            this.finish();
            if (mpasswordOperate != null){
                mpasswordOperate.closeDB();
            }
            Intent intent = new Intent(this, PasswordManage.class);
            startActivity(intent);
        }else if(check ==1){
            Toast.makeText(AddPassword.this, "云端还木有实现功能~~", Toast.LENGTH_SHORT).show();
            //云端备份
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
    }
}