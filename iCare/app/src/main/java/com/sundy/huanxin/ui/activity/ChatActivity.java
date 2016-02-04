package com.sundy.huanxin.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.sundy.huanxin.ui.fragment.EaseChatFragment;
import com.sundy.icare.R;

/**
 * Created by sundy on 16/2/4.
 */
public class ChatActivity extends FragmentActivity {

    private final String TAG = "ChatActivity";
    private EaseChatFragment chatFragment;
    private String toChatUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();
    }

    private void init() {
        //聊天人或群id
        toChatUsername = getIntent().getExtras().getString("userId");
        chatFragment = new EaseChatFragment();
        //传入参数
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        // 点击notification bar进入聊天页面，保证只有一个聊天页面
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
