package com.example.guru.pa;

/**
 * Created by Zaki on 2016/7/23.
 */
public class Info {
    public int mInfoID = 0;
    public boolean mLocal = true;
    public String mContent = "";

    public Info(int id, boolean local, String content) {
        mInfoID = id;
        mLocal = local;
        mContent = content;
    }
}
