package com.sundy.icare.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by sundy on 16/4/9.
 */
public class FileUtil {

    /**
     * sd卡文件存储路径
     */
    public static String sd_path = Environment.getExternalStorageDirectory()
            .getPath() + "/iCare/";
    //图片存储路径
    public static String sd_path_images = sd_path + "images/";
    //临时图片
    public static String sd_path_imageTmp = sd_path_images + "tempImage/";
    //缓存文件
    public static String sd_path_tmp = sd_path + "tempFile/";
    //缓存离线数据包
    public static String sd_path_tmp_offline = sd_path_tmp + "offline/";
    //语言包
    public static String sd_path_tmp_language = sd_path_tmp + "language/";


    static {
        if (sdCardExist()) {
            File file = new File(sd_path);
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File(sd_path_images);
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File(sd_path_imageTmp);
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File(sd_path_tmp);
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File(sd_path_tmp_offline);
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File(sd_path_tmp_language);
            if (!file.exists()) {
                file.mkdir();
            }
        }
    }

    /**
     * 初始化文件存储目录
     */
    public static void initSD() {
        if (sdCardExist()) {
            File file = new File(sd_path);
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File(sd_path_images);
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File(sd_path_imageTmp);
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File(sd_path_tmp);
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File(sd_path_tmp_offline);
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File(sd_path_tmp_language);
            if (!file.exists()) {
                file.mkdir();
            }
        }
    }

    // 检测是否存在SD卡
    public static boolean sdCardExist() {
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        return sdCardExist;
    }

}
