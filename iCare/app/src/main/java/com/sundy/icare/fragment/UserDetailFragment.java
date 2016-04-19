package com.sundy.icare.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.zxing.BarcodeFormat;
import com.sundy.icare.R;
import com.sundy.icare.activity.BindEmailActivity;
import com.sundy.icare.net.HttpCallback;
import com.sundy.icare.net.ResourceTaker;
import com.sundy.icare.ui.MyProgressDialog;
import com.sundy.icare.utils.EncryptionUtil;
import com.sundy.icare.utils.FileUtil;
import com.sundy.icare.utils.MyConstant;
import com.sundy.icare.utils.MyPreference;
import com.sundy.icare.utils.MyToast;
import com.sundy.icare.utils.MyUtils;
import com.sundy.icare.utils.QRCodeUtil;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

/**
 * Created by sundy on 16/4/15.
 */
public class UserDetailFragment extends LazyLoadFragment {

    private final String TAG = "UserDetailFragment";
    private LayoutInflater inflater;
    private View root;

    private SimpleDraweeView imgHeader;
    private String email, profileImage, userName, user_id, gender, label;
    private String filePath;
    private String finalImagePath; //最终图片保存路径
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
        this.inflater = inflater;
        root = inflater.inflate(R.layout.fragment_user_detail, container, false);

        aq = new AQuery(root);
        init();
        return root;
    }

    @Override
    protected void initData() {

    }

    private void init() {
        aq.id(R.id.txtTitle).text(R.string.user_detail);
        aq.id(R.id.btnBack).clicked(onClick);
        aq.id(R.id.btnQR).clicked(onClick);
        aq.id(R.id.relative_email).clicked(onClick);
        imgHeader = (SimpleDraweeView) aq.id(R.id.imgHeader).getView();
        aq.id(R.id.imgHeader).clicked(onClick);
        aq.id(R.id.relative_username).clicked(onClick);
        aq.id(R.id.relative_gender).clicked(onClick);
        aq.id(R.id.relative_address).clicked(onClick);
        aq.id(R.id.relative_tag).clicked(onClick);
        getMemberProfile();

    }

    //获取用户详情
    private void getMemberProfile() {
        SharedPreferences preferences = context.getSharedPreferences(MyPreference.Preference_User, Context.MODE_PRIVATE);
        String userId = preferences.getString(MyPreference.Preference_User_ID, "");
        String sessionKey = preferences.getString(MyPreference.Preference_User_sessionKey, "");

        showLoading();
        ResourceTaker.getMemberProfile(userId, sessionKey, userId, new HttpCallback<JSONObject>(context) {
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
                                JSONObject info = data.getJSONObject("info");
                                if (info != null) {
                                    updateUserInfo(info);
                                    showView(info);
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
    private void showView(JSONObject userInfo) {
        try {
            user_id = userInfo.has("id") ? userInfo.getString("id") : "";
            String areaCode = userInfo.has("areaCode") ? userInfo.getString("areaCode") : "";
            String phone = userInfo.has("phone") ? userInfo.getString("phone") : "";
            userName = userInfo.has("name") ? userInfo.getString("name") : "";
            profileImage = userInfo.has("profileImage") ? userInfo.getString("profileImage") : "";
            email = userInfo.has("email") ? userInfo.getString("email") : "";
            String address = userInfo.has("address") ? userInfo.getString("address") : "";
            label = userInfo.has("label") ? userInfo.getString("label") : "";
            gender = userInfo.has("gender") ? userInfo.getString("gender") : "male";

            imgHeader.setImageURI(Uri.parse(profileImage));
            aq.id(R.id.txtUsername).text(userName);
            if (gender.equals("male"))
                aq.id(R.id.txtGender).text(getString(R.string.male));
            else
                aq.id(R.id.txtGender).text(getString(R.string.female));
            aq.id(R.id.txtMobile).text(phone);
            if (email.length() == 0) {
                aq.id(R.id.txtEmail).getTextView().setHint(getString(R.string.not_yet_bind_email));
            } else {
                aq.id(R.id.txtEmail).text(email);
            }
            aq.id(R.id.txtAddress).text(address);
            aq.id(R.id.txtTag).text(label);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //更新用户信息
    private void updateUserInfo(JSONObject detail) {
        MyPreference.updateUserInfo(context, detail);
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnBack:
                    mCallback.onBack();
                    break;
                case R.id.btnQR:
                    showQRCodeDialog();
                    break;
                case R.id.relative_email:
                    showAllowBindEmailDialog();
                    break;
                case R.id.imgHeader:
                    chooseDialog();
                    break;
                case R.id.relative_username:
                    showChangeUserNameDialog();
                    break;
                case R.id.relative_gender:
                    showChangeGenderDialog();
                    break;
                case R.id.relative_address:
                    chooseAddress();
                    break;
                case R.id.relative_tag:
                    showChangeTagDialog();
                    break;
            }
        }
    };

    //显示修改标签Dialog
    private void showChangeTagDialog() {
        View view = inflater.inflate(R.layout.dialog_change_tag, null);
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(view);
        TextView btnCancel = (TextView) view.findViewById(R.id.btnCancel);
        TextView btnConfirm = (TextView) view.findViewById(R.id.btnConfirm);
        final EditText edtTag = (EditText) view.findViewById(R.id.edtTag);
        edtTag.setText(label);
        edtTag.setSelectAllOnFocus(true);
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
                String tag = edtTag.getText().toString().trim();
                showLoading();
                SharedPreferences preferences = context.getSharedPreferences(MyPreference.Preference_User, Context.MODE_PRIVATE);
                String memberID = preferences.getString(MyPreference.Preference_User_ID, "");
                String sessionKey = preferences.getString(MyPreference.Preference_User_sessionKey, "");
                ResourceTaker.updateMemberProfile(memberID, sessionKey, "label", tag, new HttpCallback<JSONObject>(context) {
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
                                        getMemberProfile();
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

    //跳转选择地址
    private void chooseAddress() {
        mCallback.addContent(new ChooseAddressFragment());
    }

    //显示修改用户性别Dialog
    private void showChangeGenderDialog() {
        View view = inflater.inflate(R.layout.dialog_change_gender, null);
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(view);

        TextView btnCancel = (TextView) view.findViewById(R.id.btnCancel);
        TextView btnConfirm = (TextView) view.findViewById(R.id.btnConfirm);
        RadioButton rb_male = (RadioButton) view.findViewById(R.id.rb_male);
        RadioButton rb_female = (RadioButton) view.findViewById(R.id.rb_female);
        RadioGroup rgGender = (RadioGroup) view.findViewById(R.id.rgGender);
        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int checkedID = radioGroup.getCheckedRadioButtonId();
                switch (checkedID) {
                    case R.id.rb_male:
                        gender = "male";
                        break;
                    case R.id.rb_female:
                        gender = "female";
                        break;
                }
            }
        });
        if (gender.equals("male")) {
            rb_male.setChecked(true);
        } else {
            rb_female.setChecked(true);
        }
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
                showLoading();
                SharedPreferences preferences = context.getSharedPreferences(MyPreference.Preference_User, Context.MODE_PRIVATE);
                String memberID = preferences.getString(MyPreference.Preference_User_ID, "");
                String sessionKey = preferences.getString(MyPreference.Preference_User_sessionKey, "");
                ResourceTaker.updateMemberProfile(memberID, sessionKey, "gender", gender, new HttpCallback<JSONObject>(context) {
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
                                        getMemberProfile();
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

    //显示修改用户名Dialog
    private void showChangeUserNameDialog() {
        View view = inflater.inflate(R.layout.dialog_change_username, null);
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(view);
        TextView btnCancel = (TextView) view.findViewById(R.id.btnCancel);
        TextView btnConfirm = (TextView) view.findViewById(R.id.btnConfirm);
        final EditText edtUserName = (EditText) view.findViewById(R.id.edtUserName);
        edtUserName.setText(userName);
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
                String username = edtUserName.getText().toString().trim();
                if (TextUtils.isEmpty(username)) {
                    MyToast.rtToast(context, getString(R.string.username_cannot_empty));
                    return;
                }
                showLoading();
                SharedPreferences preferences = context.getSharedPreferences(MyPreference.Preference_User, Context.MODE_PRIVATE);
                String memberID = preferences.getString(MyPreference.Preference_User_ID, "");
                String sessionKey = preferences.getString(MyPreference.Preference_User_sessionKey, "");
                ResourceTaker.updateMemberProfile(memberID, sessionKey, "name", username, new HttpCallback<JSONObject>(context) {
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
                                        getMemberProfile();
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

    //显示二维码Dialog
    private void showQRCodeDialog() {
        View view = inflater.inflate(R.layout.dialog_show_qr_code, null);
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

    //显示允许绑定邮箱Dialog
    private void showAllowBindEmailDialog() {
        View view = inflater.inflate(R.layout.dialog_allow_doing_something, null);
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(view);
        TextView btnCancel = (TextView) view.findViewById(R.id.btnCancel);
        TextView btnConfirm = (TextView) view.findViewById(R.id.btnConfirm);
        final EditText edtPassword = (EditText) view.findViewById(R.id.edtPassword);
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
                String password = edtPassword.getText().toString().trim();
                if (TextUtils.isEmpty(password)) {
                    MyToast.rtToast(context, getString(R.string.login_password_cannot_empty));
                    return;
                }
                password = EncryptionUtil.getMD5(context, password);
                SharedPreferences preferences = context.getSharedPreferences(MyPreference.Preference_User, Context.MODE_PRIVATE);
                String savedPwd = preferences.getString(MyPreference.Preference_User_Login_password, "");
                if (password.equals(savedPwd)) {
                    dialog.dismiss();
                    dialog.cancel();
                    goBindEmail();
                } else {
                    MyToast.rtToast(context, getString(R.string.password_is_wrong));
                }
            }
        });

        dialog.show();
    }

    //跳转绑定邮箱页面
    private void goBindEmail() {
        Intent intent = new Intent(context, BindEmailActivity.class);
        startActivityForResult(intent, MyConstant.REQUEST_CODE_BIND_EMAIL);
        context.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    //图片选择Dialog
    private void chooseDialog() {
        View view = inflater.inflate(R.layout.dialog_choose_photo, null);
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(view);
        TextView btn_album = (TextView) view.findViewById(R.id.btn_album);
        TextView btn_camera = (TextView) view.findViewById(R.id.btn_camera);
        ImageButton btn_close = (ImageButton) view.findViewById(R.id.btn_close);
        btn_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, MyConstant.IMAGE_PHOTO_ALBUM);
            }
        });
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                String fileName = MyUtils.getTimeStamp();
                filePath = FileUtil.sd_path_imageTmp + fileName + ".jpg";
                File file = new File(filePath);
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(intent, MyConstant.IMAGE_CAMERA);
            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
            }
        });
        dialog.show();
    }

    //截图
    private void cropImage(Uri uri, int outputX, int outputY) {
        finalImagePath = FileUtil.sd_path_imageTmp + "header_" + MyUtils.getTimeStamp() + ".jpg";
        File outFile = new File(finalImagePath);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("output", Uri.fromFile(outFile));
        startActivityForResult(intent, MyConstant.RESULT_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            Uri uri = null;
            switch (requestCode) {
                case MyConstant.REQUEST_CODE_BIND_EMAIL:
                    getMemberProfile();
                    break;
                // 相册
                case MyConstant.IMAGE_PHOTO_ALBUM:
                    if (data != null) {
                        uri = data.getData();
                        cropImage(uri, 300, 300);
                    }
                    break;
                //拍照
                case MyConstant.IMAGE_CAMERA:
                    if (resultCode == -1) {
                        if (filePath != null) {
                            File file = new File(filePath);
                            if (file.exists()) {
                                if (file.length() > 0) {
                                    uri = Uri.fromFile(file);
                                    cropImage(uri, 400, 400);
                                }
                            }
                        }
                    }
                    break;
                case MyConstant.RESULT_REQUEST_CODE:
                    imgHeader.setImageURI(Uri.parse("file://" + finalImagePath));
                    updateUserHeader();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //修改用户头像
    private void updateUserHeader() {
        if (finalImagePath == null || finalImagePath.length() == 0) {
            return;
        }
        File file = new File(finalImagePath);
        showLoading();
        SharedPreferences preferences = context.getSharedPreferences(MyPreference.Preference_User, Context.MODE_PRIVATE);
        String memberID = preferences.getString(MyPreference.Preference_User_ID, "");
        String sessionKey = preferences.getString(MyPreference.Preference_User_sessionKey, "");
        ResourceTaker.updateMemberProfileHeader(memberID, sessionKey, "profileImage", file, new HttpCallback<JSONObject>(context) {
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
                                getMemberProfile();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
