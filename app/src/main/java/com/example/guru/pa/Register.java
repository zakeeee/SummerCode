package com.example.guru.pa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;
import android.database.Cursor;
public class Register extends AppCompatActivity {
    private EditText reg_username_edit;
    private EditText reg_password1_edit;
    private EditText reg_password2_edit;
    private Button button;

    public static SQLiteDatabase db;//SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        reg_username_edit = (EditText)findViewById(R.id.reg_username_edit);
        reg_password1_edit = (EditText)findViewById(R.id.reg_password1_edit);
        reg_password2_edit = (EditText)findViewById(R.id.reg_password2_edit);
        button = (Button)findViewById(R.id.button);
        db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString()+"/test.dbs", null);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = reg_username_edit.getText().toString();
                String password = reg_password1_edit.getText().toString();
                String password1 = reg_password2_edit.getText().toString();
                if (name.equals("") || password.equals("")||password1.equals("")){
                    Toast.makeText(Register.this,"请将帐号密码填完整", Toast.LENGTH_SHORT).show();
                }else{
                    try{
                        Cursor cursor = db.rawQuery("select * from tb_user where name=?",new String []{name});
                        if(cursor.getCount()>0)
                            Toast.makeText(Register.this,"用户名已存在", Toast.LENGTH_SHORT).show();
                    }catch (Exception e) {
                        db.execSQL("create table tb_user( name varchar(30) primary key,password varchar(30))");
                    }
                    if(password.equals(password1)){
                        if (addUser(name, password)) {
                            DialogInterface.OnClickListener ss = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,int which) {
                                    // 跳转到登录界面
                                    Intent in = new Intent();
                                    in.setClass(Register.this,
                                            LogIn.class);
                                    startActivity(in);
                                    Register.this.finish();
                                }
                            };
                            new AlertDialog.Builder(Register.this)
                                    .setTitle("注册成功").setMessage("注册成功")
                                    .setPositiveButton("确定", ss).show();
                            //     Register.this.onDestroy();

                        } else {
                            Toast.makeText(Register.this,"注册失败", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(Register.this,"两次输入密码不一致", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    // 添加用户
    public Boolean addUser(String name, String password) {
        String str = "insert into tb_user values(?,?) ";
        LogIn usr = new LogIn();usr.db = db;
        try{
            db.execSQL(str, new String[]{name, password});
            return true;
        }catch(Exception e)    {
            return false;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
