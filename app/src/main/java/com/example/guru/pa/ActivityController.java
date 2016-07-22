package com.example.guru.pa;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Zaki on 2016/7/21.
 */
public class ActivityController {

    public static void jumpToAnotherActivity(Context from, Class<?> to) {
        Intent intent = new Intent(from, to);
        from.startActivity(intent);
    }
}
