package com.sundy.icare.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by sundy on 16/1/17.
 */
public class MyToast {

    private final String TAG = "MyToast";

    public static void rtToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
