package com.example.guru.pa;

import android.content.Intent;
import android.os.Bundle;
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
public class LogIn extends AppCompatActivity {

    private EditText edname;
    private EditText edpassword;
    private Button login;
    public static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /* 添加返回按钮 */
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        userLogIn();

    }

    /**
     * 创建数据库
     * */
    public void createDb() {
        db.execSQL("create table tb_user( name varchar(30) primary key,password varchar(30))");
    }

    /**
     * 登录
     * */
    private void userLogIn() {

        edname = (EditText) findViewById(R.id.editText);
        edpassword = (EditText) findViewById(R.id.editText2);
        login = (Button) findViewById(R.id.login);
        db = SQLiteDatabase.openOrCreateDatabase(LogIn.this.getFilesDir().toString()+ "/test.dbs", null);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edname.getText().toString();
                String password = edpassword.getText().toString();
                if(name.equals("") || password.equals("")){
                    new AlertDialog.Builder(LogIn.this).setTitle("错误")
                            .setMessage("帐号或密码不能空").setPositiveButton("确定", null)
                            .show();
                }else{
                    isUserinfo(name, password);
                }
            }
            public Boolean isUserinfo(String name, String pwd) {
                try{
                    String str="select name, password from tb_user where name=? and password=?";
                    Cursor cursor = db.rawQuery(str, new String []{name,pwd});
                    if(cursor.getCount()<=0){
                        new AlertDialog.Builder(LogIn.this).setTitle("错误")
                                .setMessage("帐号或密码错误！").setPositiveButton("确定", null)
                                .show();
                        return false;
                    }else{
                        Toast.makeText(LogIn.this,"登录成功", Toast.LENGTH_SHORT).show();
                        // 跳转到主界面
                        MainActivity.LOGGEDIN = true;
                        MainActivity.USERNAME = name;
                        Intent intent = new Intent(LogIn.this, MainActivity.class);
                        startActivity(intent);
                    }
                }catch(SQLiteException e){
                    createDb();
                }
                return false;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                Intent intent = new Intent(LogIn.this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
