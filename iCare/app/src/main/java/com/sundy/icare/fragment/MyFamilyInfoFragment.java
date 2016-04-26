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
public class MyFamilyInfoFragment extends LazyLoadFragment {

    private final String TAG = "MyFamilyInfoFragment";
    private View root;
    private String id;
    private SimpleDraweeView img;
    private String familyId;
    private String profileImage;
    private String userName;
    private JSONObject detail;

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
        root = mInflater.inflate(R.layout.fragment_my_family_info, container, false);
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
        aq.id(R.id.relative_More).clicked(onClick);
        aq.id(R.id.imgQR).clicked(onClick);
        aq.id(R.id.rel_User).clicked(onClick);
        aq.id(R.id.txt_Comment).clicked(onClick);

        img = (SimpleDraweeView) aq.id(R.id.img).getView();
        id = getArguments().getString("id");
        getFamilyDetail();

    }

    //获取家人详情
    private void getFamilyDetail() {
        ResourceTaker.getMemberProfile(id, new HttpCallback<JSONObject>(context) {
            @Override
            public void callback(String url, JSONObject data, String status) {
                super.callback(url, data, status);
                try {
                    if (data != null) {
                        JSONObject result = data.getJSONObject("result");
                        if (result != null) {
                            String code = result.getString("code");
                            String message = result.getString("message");
                            if (code.equals("1000")) {
                                JSONObject info = data.getJSONObject("info");
                                if (info != null) {
                                    detail = info;
                                    showDetail(info);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //显示详情
    private void showDetail(JSONObject info) {
        try {
            userName = info.getString("name");
            String mobile = info.getString("phone");
            profileImage = info.getString("profileImage");
            familyId = info.getString("id");
            aq.id(R.id.txtName).text(userName);
            aq.id(R.id.txtMobile).text(getString(R.string.mobile) + " : " + mobile);
            img.setImageURI(Uri.parse(profileImage));
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
                case R.id.imgQR:
                    showQRCodeDialog();
                    break;
                case R.id.rel_User:
                    goMyFamilyDetail(detail);
                    break;
                case R.id.txt_Comment:
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
        edtRemark.setText(userName);
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
                String remark = edtRemark.getText().toString().trim();
                showLoading();
                ResourceTaker.changeFamilyRemark(familyId, remark, new HttpCallback<JSONObject>(context) {
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
                                        getFamilyDetail();
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

    //跳转我的家人详情页
    private void goMyFamilyDetail(JSONObject detail) {
        MyFamilyDetailFragment fragment = new MyFamilyDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("data", detail.toString());
        fragment.setArguments(bundle);
        mCallback.addContent(fragment);
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
        txtName.setText(userName);
        try {
            Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.ease_ic_launcher);
            int width = MyConstant.SCREEN_WIDTH;
            width = width - MyUtils.dip2px(context, 30);
            //获取二维码
            Bitmap bm = QRCodeUtil.createQRCode(familyId, logo, BarcodeFormat.QR_CODE, width, width);
            //将二维码在界面中显示
            imgQR.setImageBitmap(bm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dialog.show();
    }
}
