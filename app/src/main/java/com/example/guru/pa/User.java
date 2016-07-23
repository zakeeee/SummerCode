package com.example.guru.pa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Zaki on 2016/7/22.
 * 用户信息
 */
public class User {

    public static String mUsername = "请登陆";
    public static String mNickname = "";
    public static int mSex = 0;
    public static String mExtra = "";
    public static Image mHeadPortrait = null;
    public static Boolean mLoggedIn = false;
    public static String mToken = "";
    public static SharedPreferences mSharedPre;
    public static String INIFILENAME = "userinfo";

    /* 用户登陆请求参数 */
    public static RequestParams userLogIn(String uname, String passwd){
        RequestParams requestParams = new RequestParams();
        requestParams.add("username",uname);
        requestParams.add("password",passwd);

        return requestParams;
    }

    /* 用户登出请求参数 */
    public static RequestParams userLogout(){
        RequestParams requestParams = new RequestParams();
        requestParams.add("username",mUsername);
        requestParams.add("token",mToken);

        return requestParams;
    }

    /* 用户注册请求参数 */
    public static RequestParams userRegist(String uname, String passwd){
        RequestParams requestParams = new RequestParams();
        requestParams.add("username",uname);
        requestParams.add("password",passwd);

        return requestParams;
    }

    /* 用户获取相关信息请求参数 */
    public static RequestParams userGetInfo(){
        RequestParams requestParams = new RequestParams();
        requestParams.add("username",mUsername);

        return requestParams;
    }

    /* 用户上传行程请求参数 */
    public static RequestParams userUploadJourney(Map<String,String> map){
        RequestParams requestParams = new RequestParams(map);
        requestParams.add("username",mUsername);
        requestParams.add("token",mToken);

        return requestParams;
    }

    /* 用户下载行程请求参数 */
    public static RequestParams userDownloadJourney(){
        RequestParams requestParams = new RequestParams();
        requestParams.add("username",mUsername);
        requestParams.add("token",mToken);

        return requestParams;
    }

    /* 用户上传账单请求参数 */
    public static RequestParams userUploadBill(Map<String,String> map){
        RequestParams requestParams = new RequestParams(map);
        requestParams.add("username",mUsername);
        requestParams.add("token",mToken);

        return requestParams;
    }

    /* 用户下载账单请求参数 */
    public static RequestParams userDownloadBill(){
        RequestParams requestParams = new RequestParams();
        requestParams.add("username",mUsername);
        requestParams.add("token",mToken);

        return requestParams;
    }

    /* 用户上传密码请求参数 */
    public static RequestParams userUploadPassword(Map<String,String> map){
        RequestParams requestParams = new RequestParams(map);
        requestParams.add("username",mUsername);
        requestParams.add("token",mToken);

        return requestParams;
    }

    /* 用户下载密码请求参数 */
    public static RequestParams userDownloadPassword(){
        RequestParams requestParams = new RequestParams();
        requestParams.add("username",mUsername);
        requestParams.add("token",mToken);

        return requestParams;
    }

    /* 重置用户数据请求参数 */
    public static void userReset() {
        mUsername = "请登陆";
        mNickname = "";
        mSex = 0;
        mExtra = "";
        mHeadPortrait = null;
        mLoggedIn = false;
        mToken = "";
    }

    /* 初始化用户数据 */
    public static void userSet() {
        mUsername = mSharedPre.getString("username","请登陆");
        mNickname = mSharedPre.getString("nickname","");
        mSex = mSharedPre.getInt("sex",0);
        mExtra = mSharedPre.getString("extra","");
        mLoggedIn = mSharedPre.getBoolean("loggedin",false);
        mToken = mSharedPre.getString("token","");
    }

    /* 保存用户数据 */
    public static void userSave() {
        SharedPreferences.Editor editor = mSharedPre.edit();
        editor.putString("username",mUsername);
        editor.putString("nickname",mNickname);
        editor.putInt("sex",mSex);
        editor.putString("extra",mExtra);
        editor.putBoolean("loggedin",mLoggedIn);
        editor.putString("token",mToken);
        editor.commit();
    }

}
