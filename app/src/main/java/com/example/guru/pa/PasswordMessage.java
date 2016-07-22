package com.example.guru.pa;

/**
 * Created by Crystal on 2016/7/22.
 */
public class PasswordMessage {
    private int id;
    private String purpose;
    private String username;
    private String password;
    private String extra;

    public PasswordMessage(int id, String purpose, String username, String password, String extra){
        this.id = id;
        this.purpose = purpose;
        this.username = username;
        this.password = password;
        this.extra = extra;
    }

    public PasswordMessage(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
