package com.example.guru.pa;

import android.text.BoringLayout;

/**
 * Created by Crystal on 2016/7/22.
 */
public class PasswordMessage {
    private Integer mID;
    private Boolean mLocal;
    private String mPurpose;
    private String mUsername;
    private String mPassword;
    private String mExtra;

    public PasswordMessage(int id,boolean local, String purpose, String username, String password, String extra){
        this.mID = id;
        this.mLocal = local;
        this.mPurpose = DES3Utils.encryptMode(purpose);
        this.mUsername = DES3Utils.encryptMode(username);
        this.mPassword = DES3Utils.encryptMode(password);
        this.mExtra = DES3Utils.encryptMode(extra);
    }

    public PasswordMessage(){}

    @Override
    public String toString() {
        String str;
        if(this.mLocal) {
            str = "<本地>"+"\n"
                    +"用途："+DES3Utils.decryptMode(mPurpose)+"\n"
                    +"账号："+DES3Utils.decryptMode(mUsername)+"\n"
                    +"密码："+DES3Utils.decryptMode(mPassword)+"\n"
                    +"备注："+DES3Utils.decryptMode(mExtra);
        } else {
            str = "<云端>"+"\n"
                    +"用途："+DES3Utils.decryptMode(mPurpose)+"\n"
                    +"账号："+DES3Utils.decryptMode(mUsername)+"\n"
                    +"密码："+DES3Utils.decryptMode(mPassword)+"\n"
                    +"备注："+DES3Utils.decryptMode(mExtra);
        }
        return str;
    }

    public int getId() {
        return mID;
    }

    public void setId(int id) {
        this.mID = id;
    }

    public boolean getLocal() {
        return mLocal;
    }

    public void setLocal(boolean local) {
        this.mLocal = local;
    }

    public String getPurpose() {
        return DES3Utils.decryptMode(mPurpose);
    }

    public void setPurpose(String purpose) { this.mPurpose = DES3Utils.encryptMode(purpose); }

    public String getUsername() {
        return DES3Utils.decryptMode(mUsername);
    }

    public void setUsername(String username) {
        this.mUsername = DES3Utils.encryptMode(username);
    }

    public String getPassword() {
        return DES3Utils.decryptMode(mPassword);
    }

    public void setPassword(String password) {
        this.mPassword = DES3Utils.encryptMode(password);
    }

    public String getExtra() {
        return DES3Utils.decryptMode(mExtra);
    }

    public void setExtra(String extra) {
        this.mExtra = DES3Utils.encryptMode(extra);
    }
}
