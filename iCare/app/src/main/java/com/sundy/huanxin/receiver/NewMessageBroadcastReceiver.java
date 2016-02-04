package com.sundy.huanxin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.sundy.icare.utils.MyUtils;

/**
 * 接收新消息的监听广播
 * Created by sundy on 16/2/4.
 */
public class NewMessageBroadcastReceiver extends BroadcastReceiver {

    private final String TAG = "NewMessageBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // 注销广播
        abortBroadcast();
        // 消息id（每条消息都会生成唯一的一个id，目前是SDK生成）
        String msgId = intent.getStringExtra("msgid");
        //发送方
        String username = intent.getStringExtra("from");
        // 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
        EMMessage message = EMChatManager.getInstance().getMessage(msgId);
        EMConversation conversation = EMChatManager.getInstance().getConversation(username);

        MyUtils.rtLog(TAG, "-------->msgId =" + msgId);
        MyUtils.rtLog(TAG, "-------->username =" + username);

        // 如果是群聊消息，获取到group id
        if (message.getChatType() == EMMessage.ChatType.GroupChat) {
            username = message.getTo();
            MyUtils.rtLog(TAG, "-------->username from group =" + username);
        }
        if (!username.equals(username)) {
            // 消息不是发给当前会话，return
            return;
        }
    }
}
