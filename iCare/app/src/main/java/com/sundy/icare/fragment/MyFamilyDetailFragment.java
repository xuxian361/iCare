package com.sundy.icare.fragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.zxing.BarcodeFormat;
import com.sundy.icare.R;
import com.sundy.icare.net.HttpCallback;
import com.sundy.icare.net.ResourceTaker;
import com.sundy.icare.ui.MyProgressDialog;
import com.sundy.icare.utils.MyConstant;
import com.sundy.icare.utils.MyToast;
import com.sundy.icare.utils.MyUtils;
import com.sundy.icare.utils.QRCodeUtil;

import org.json.JSONObject;

/**
 * Created by sundy on 16/4/25.
 */
public class MyFamilyDetailFragment extends LazyLoadFragment {

    private final String TAG = "MyFamilyDetailFragment";
    private View root;

    private SimpleDraweeView imgHeader;
    private JSONObject detail;
    private String profileImage, name, id;

    private MyProgressDialog progressDialog;

    public void showLoading() {
        MyUtils.rtLog(TAG, "-------->showLoading");
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        progressDialog = new MyProgressDialog(context, context.getWindow().getDecorView());
        progressDialog.show();
    }

    public void closeLoading() {
        MyUtils.rtLog(TAG, "-------->closeLoading");
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        root = mInflater.inflate(R.layout.fragment_my_family_detail, container, false);
        aq = new AQuery(root);

        init();

        return root;
    }

    @Override
    protected void initData() {

    }

    private void init() {
        aq.id(R.id.btnBack).clicked(onClick);
        aq.id(R.id.txtTitle).text(R.string.detail_info);
        aq.id(R.id.btnRight).clicked(onClick);
        aq.id(R.id.btnQR).clicked(onClick);
        aq.id(R.id.relative_More).clicked(onClick);
        aq.id(R.id.linear_Remark).clicked(onClick);

        imgHeader = (SimpleDraweeView) aq.id(R.id.imgHeader).getView();

        try {
            String data = getArguments().getString("data");

            detail = new JSONObject(data);
            id = detail.getString("id");
            name = detail.getString("name");
            String phone = detail.getString("phone");
            profileImage = detail.getString("profileImage");

            aq.id(R.id.txtUsername).text(name);
            aq.id(R.id.txtMobile).text(phone);
            imgHeader.setImageURI(Uri.parse(profileImage));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnBack:
                    mCallback.onBack();
                    break;
                case R.id.btnRight:
                    int visibility = aq.id(R.id.relative_More).getView().getVisibility();
                    if (visibility == View.GONE) {
                        aq.id(R.id.relative_More).visible();
                    } else {
                        aq.id(R.id.relative_More).gone();
                    }
                    break;
                case R.id.relative_More:
                    aq.id(R.id.relative_More).gone();
                    break;
                case R.id.btnQR:
                    showQRCodeDialog();
                    break;
                case R.id.linear_Remark:
                    showSetRemarkDialog();
                    break;
            }
        }
    };

    //显示设置备注Dialog
    private void showSetRemarkDialog() {
        View view = mInflater.inflate(R.layout.dialog_change_remark, null);
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(view);
        TextView btnCancel = (TextView) view.findViewById(R.id.btnCancel);
        TextView btnConfirm = (TextView) view.findViewById(R.id.btnConfirm);
        final EditText edtRemark = (EditText) view.findViewById(R.id.edtRemark);
        edtRemark.setText(name);
        edtRemark.setSelectAllOnFocus(true);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String remark = edtRemark.getText().toString().trim();
                showLoading();
                ResourceTaker.changeFamilyRemark(id, remark, new HttpCallback<JSONObject>(context) {
                    @Override
                    public void callback(String url, JSONObject data, String status) {
                        super.callback(url, data, status);
                        closeLoading();
                        try {
                            if (data != null) {
                                JSONObject result = data.getJSONObject("result");
                                if (result != null) {
                                    String code = result.getString("code");
                                    String message = result.getString("message");
                                    if (code.equals("1000")) {
                                        dialog.dismiss();
                                        dialog.cancel();
                                        aq.id(R.id.txtUsername).text(remark);
                                    } else {
                                        MyToast.rtToast(context, message);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });

        dialog.show();
    }

    //显示用户二维码Dialog
    private void showQRCodeDialog() {
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
        txtName.setText(name);
        try {
            Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.ease_ic_launcher);
            int width = MyConstant.SCREEN_WIDTH;
            width = width - MyUtils.dip2px(context, 30);
            //获取二维码
            Bitmap bm = QRCodeUtil.createQRCode(id, logo, BarcodeFormat.QR_CODE, width, width);
            //将二维码在界面中显示
            imgQR.setImageBitmap(bm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dialog.show();
    }
}
