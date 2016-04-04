package com.sundy.icare.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.sundy.icare.R;
import com.sundy.icare.views.activity.LoadingActivity;
import com.sundy.icare.views.activity.MainActivity;

/**
 * Created by sundy on 16/4/4.
 */
public class NotificationHelper {

    public static final int NOTIFICATION_ID = 1;

    public static void showMessageNotification(Context context, NotificationManager nm, String title, String message, String channel) {
        if (message == null) {
            return;
        }
        if (nm == null) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, LoadingActivity.class), 0);
        if (message.equals("action:login")) {
            System.out.print("login");
            contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, LoadingActivity.class), 0);
        } else if (message.equals("action:main")) {
            contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
            System.out.print("main");
        }
        Notification notify2 = new Notification.Builder(context)
                .setSmallIcon(R.drawable.launcher_icon)
                .setTicker("TickerText:" + "您有新短消息，请注意查收！")
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(contentIntent)
                .setNumber(1)
                .getNotification();
        notify2.flags |= Notification.FLAG_AUTO_CANCEL | Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
        nm.notify(NOTIFICATION_ID, notify2);

    }

}
