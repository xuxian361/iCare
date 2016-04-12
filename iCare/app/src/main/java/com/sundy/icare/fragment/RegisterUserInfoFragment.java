package com.sundy.icare.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sundy.icare.R;
import com.sundy.icare.utils.FileUtil;
import com.sundy.icare.utils.MyPreference;
import com.sundy.icare.utils.MyToast;
import com.sundy.icare.utils.MyUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by sundy on 16/4/12.
 */
public class RegisterUserInfoFragment extends BaseFragment {

    private LayoutInflater inflater;
    private View root;

    private final String TAG = "RegisterUserInfoFragment";
    private EditText edtUserName;
    private final int IMAGE_PHOTO_ALBUM = 1;
    private final int IMAGE_CAMERA = 2;
    private final int RESULT_REQUEST_CODE = 3;
    private String filePath;
    private SimpleDraweeView imgHeader;
    private String finalImagePath; //最终图片保存路径
    private final int Gender_Male = 1;
    private final int Gender_Female = 2;
    private String gender;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        root = inflater.inflate(R.layout.fragment_register_userinfo, container, false);

        aq = new AQuery(root);
        init();
        return root;
    }

    private void init() {
        aq.id(R.id.txtTitle).text(R.string.register);
        aq.id(R.id.txtRight).text(R.string.next_step).clicked(onClick);
        aq.id(R.id.btn_back).clicked(onClick);
        imgHeader = (SimpleDraweeView) aq.id(R.id.imgHeader).getView();
        aq.id(R.id.imgHeader).clicked(onClick);
        edtUserName = aq.id(R.id.edtUserName).getEditText();
        aq.id(R.id.btn_male).clicked(onClick);
        aq.id(R.id.btn_female).clicked(onClick);
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_back:
                    mCallback.onBack();
                    break;
                case R.id.txtRight:
                    saveRegisterInfo();
                    break;
                case R.id.imgHeader:
                    chooseDialog();
                    break;
                case R.id.btn_male:
                    gender = "male";
                    changeGenderState(Gender_Male);
                    break;
                case R.id.btn_female:
                    gender = "female";
                    changeGenderState(Gender_Female);
                    break;
            }
        }
    };

    //改变性别选择状态
    private void changeGenderState(int gender) {
        if (gender == Gender_Male) {//先生
            aq.id(R.id.txt_male).textColor(Color.parseColor("#0066FF"));
            aq.id(R.id.txt_female).textColor(Color.parseColor("#999999"));
            aq.id(R.id.img_male).image(R.mipmap.icon_fav);
            aq.id(R.id.img_female).image(R.mipmap.icon_local_black);
        } else if (gender == Gender_Female) {//女士
            aq.id(R.id.txt_male).textColor(Color.parseColor("#999999"));
            aq.id(R.id.txt_female).textColor(Color.parseColor("#ff4081"));
            aq.id(R.id.img_male).image(R.mipmap.icon_fav1);
            aq.id(R.id.img_female).image(R.mipmap.icon_location_white);
        }
    }

    //保存用户注册信息
    private void saveRegisterInfo() {
        String username = edtUserName.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            MyToast.rtToast(context, getString(R.string.username_cannot_empty));
            return;
        }
        if (TextUtils.isEmpty(gender)) {
            MyToast.rtToast(context, getString(R.string.choose_gender));
            return;
        }
        if (TextUtils.isEmpty(finalImagePath)) {
            finalImagePath = "";
        }
        SharedPreferences preferences = context.getSharedPreferences(MyPreference.Preference_Registration, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(MyPreference.Preference_Registration_username, username);
        editor.putString(MyPreference.Preference_Registration_gender, gender);
        editor.putString(MyPreference.Preference_Registration_profileImage, finalImagePath);
        editor.commit();

        go2RegisterMobile();
    }

    //跳转注册手机页
    private void go2RegisterMobile() {
        mCallback.addContent(new RegisterMobileFragment());
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
                startActivityForResult(intent, IMAGE_PHOTO_ALBUM);
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
                startActivityForResult(intent, IMAGE_CAMERA);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = null;
        switch (requestCode) {
            // 相册
            case IMAGE_PHOTO_ALBUM:
                if (data != null) {
                    uri = data.getData();
                    cropImage(uri, 300, 300);
                }
                break;
            //拍照
            case IMAGE_CAMERA:
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
            case RESULT_REQUEST_CODE:
                imgHeader.setImageURI(Uri.parse("file://" + finalImagePath));
                break;
        }
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
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
