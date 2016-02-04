package com.sundy.huanxin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.sundy.icare.utils.MyUtils;

/**
 * 接收ack回执消息的BroadcastReceiver
 * Created by sundy on 16/2/4.
 */
public class AckMessageBroadcastReceiver extends BroadcastReceiver {

    private final String TAG = "AckMessageBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        abortBroadcast();
        String msgid = intent.getStringExtra("msgid");
        String from = intent.getStringExtra("from");
        MyUtils.rtLog(TAG, "-------->msgid = " + msgid);
        MyUtils.rtLog(TAG, "-------->from = " + from);
        EMConversation conversation = EMChatManager.getInstance().getConversation(from);
        if (conversation != null) {
            // 把message设为已读
            EMMessage msg = conversation.getMessage(msgid);
            if (msg != null) {
                msg.isAcked = true;
            }
        }
    }
}
