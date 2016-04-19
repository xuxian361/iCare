package com.sundy.icare.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.zxing.BarcodeFormat;
import com.sundy.icare.R;
import com.sundy.icare.activity.QRScannerActivity;
import com.sundy.icare.utils.MyConstant;
import com.sundy.icare.utils.MyPreference;
import com.sundy.icare.utils.MyUtils;
import com.sundy.icare.utils.QRCodeUtil;

/**
 * Created by sundy on 16/4/18.
 */
public class AddFamilyFragment extends LazyLoadFragment {

    private final String TAG = "AddFamilyFragment";
    private View root;

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        root = mInflater.inflate(R.layout.fragment_add_family, container, false);
        aq = new AQuery(root);

        init();

        return root;
    }

    @Override
    protected void initData() {

    }

    private void init() {
        aq.id(R.id.txtTitle).text(R.string.add_family);
        aq.id(R.id.btnBack).clicked(onClick);
        aq.id(R.id.rel_Register).clicked(onClick);
        aq.id(R.id.rel_Scanner).clicked(onClick);
        aq.id(R.id.linear_qrCode).clicked(onClick);
        aq.id(R.id.relative_search).clicked(onClick);

    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnBack:
                    mCallback.onBack();
                    break;
                case R.id.rel_Register:
                    registerFamily();
                    break;
                case R.id.rel_Scanner:
                    scanQRCode();
                    break;
                case R.id.linear_qrCode:
                    showQRCodeDialog();
                    break;
                case R.id.relative_search:
                    goSearchPage();
                    break;
            }
        }
    };

    //跳转搜索页面
    private void goSearchPage() {
        mCallback.addContent(new SearchFragment());
    }

    //跳转注册家人页面
    private void registerFamily() {
        mCallback.addContent(new BindFamilyFragment());
    }

    //跳转打开二维码扫描器
    private void scanQRCode() {
        Intent intent = new Intent(getActivity(), QRScannerActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    //显示二维码Dialog
    private void showQRCodeDialog() {
        SharedPreferences preferences = context.getSharedPreferences(MyPreference.Preference_User, Context.MODE_PRIVATE);
        String userName = preferences.getString(MyPreference.Preference_User_name, "");
        String profileImage = preferences.getString(MyPreference.Preference_User_profileImage, "");
        String user_id = preferences.getString(MyPreference.Preference_User_ID, "");

        View view = mInflater.inflate(R.layout.dialog_show_qr_code, null);
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(view);
        SimpleDraweeView header = (SimpleDraweeView) view.findViewById(R.id.imgHeader);
        LinearLayout linearContent = (LinearLayout) view.findViewById(R.id.linearContent);
        TextView txtName = (TextView) view.findViewById(R.id.txtName);
        ImageView imgQR = (ImageView) view.findViewById(R.id.imgQR);
        linearContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
            }
        });
        header.setImageURI(Uri.parse(profileImage));
        txtName.setText(userName);
        try {
            Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.ease_ic_launcher);
            int width = MyConstant.SCREEN_WIDTH;
            width = width - MyUtils.dip2px(context, 30);
            //获取二维码
            Bitmap bm = QRCodeUtil.createQRCode(user_id, logo, BarcodeFormat.QR_CODE, width, width);
            //将二维码在界面中显示
            imgQR.setImageBitmap(bm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dialog.show();
    }

}
