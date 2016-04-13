package com.sundy.icare.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.androidquery.AQuery;
import com.sundy.icare.R;
import com.sundy.icare.utils.MyConstant;
import com.sundy.icare.utils.MyUtils;

/**
 * Created by sundy on 16/4/13.
 */
public class MyProgressDialog {
    private View mParent;
    private Activity context;
    private LayoutInflater inflater;
    private View view;
    private AQuery aq;
    private PopupWindow mPopupWindow;
    private RelativeLayout layout;

    public MyProgressDialog() {

    }

    public MyProgressDialog(Activity context, View parent) {
        this.context = context;
        this.mParent = parent;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.dialog_loading, null);
        aq = new AQuery(view);
        layout = (RelativeLayout) aq.id(R.id.relative_loading).getView();

        layout.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keycode, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keycode == KeyEvent.KEYCODE_BACK) {
                    dismiss();
                }
                return false;
            }
        });

    }

    //显示Dailog
    public void show() {
        layout.setVisibility(View.VISIBLE);

        int width = MyConstant.SCREEN_WIDTH;
        int height = MyConstant.SCREEN_HEIGHT;

        if (mPopupWindow == null) {
            mPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setWidth(width);
            mPopupWindow.setHeight(height);
        }

        mPopupWindow.showAtLocation(mParent, Gravity.CENTER, 0, 0);
    }

    //取消Dailog的显示
    public void dismiss() {
        layout.setVisibility(View.GONE);
        if (mPopupWindow == null)
            return;
        mPopupWindow.dismiss();
        mPopupWindow = null;

    }

}