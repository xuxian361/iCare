package com.sundy.icare.utils;

import android.util.DisplayMetrics;

/**
 * 常量类
 * <p/>
 * Created by sundy on 15/12/6.
 */
public class MyConstant {


    public static final String APP_NAME = "iCare";

    public static int SCREEN_WIDTH = 720;
    public static int SCREEN_HEIGHT = 1080;
    public static float SCREEN_DENSITY = DisplayMetrics.DENSITY_HIGH;


    public static final String APP_VERSION = "1.0";
    public static final String APP_LANGUAGE = "sc";
    public static final String SYSTEM_TYPE = "android";
    public static final String USER_TYPE = "children";

    //打印日志Log标识
    public static final boolean IsDebug = true;


    //Request Code
    public static final int IMAGE_PHOTO_ALBUM = 1;
    public static final int IMAGE_CAMERA = 2;
    public static final int RESULT_REQUEST_CODE = 3;
    public static final int REQUEST_CODE_BIND_EMAIL = 4;

}
