package com.sundy.icare.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sundy.icare.R;
import com.sundy.icare.net.HttpCallback;
import com.sundy.icare.net.ResourceTaker;
import com.sundy.icare.ui.MyProgressDialog;
import com.sundy.icare.utils.FileUtil;
import com.sundy.icare.utils.MyConstant;
import com.sundy.icare.utils.MyToast;
import com.sundy.icare.utils.MyUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

/**
 * Created by sundy on 16/4/17.
 */
public class BindFamilyFragment extends LazyLoadFragment {

    private final String TAG = "BindFamilyFragment";
    private View root;
    private RadioGroup rg_gender;
    private String areaCode = "86";
    private String gender = "male";
    private String filePath;
    private SimpleDraweeView imgHeader;
    private String finalImagePath; //最终图片保存路径
    private TextView txtBirthday;
    private String birthday; //生日

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
        root = mInflater.inflate(R.layout.fragment_bind_family, container, false);
        aq = new AQuery(root);

        init();

        return root;
    }

    @Override
    protected void initData() {

    }

    private void init() {
        aq.id(R.id.txtTitle).text(R.string.bind_family);
        aq.id(R.id.btnBack).clicked(onClick);
        aq.id(R.id.txtRight).text(getString(R.string.submit)).clicked(onClick);
        imgHeader = (SimpleDraweeView) aq.id(R.id.imgHeader).getView();
        aq.id(R.id.imgHeader).clicked(onClick);
        aq.id(R.id.rel_Address).clicked(onClick);
        aq.id(R.id.rel_Birthday).clicked(onClick);

        txtBirthday = aq.id(R.id.txtBirthday).getTextView();
        birthday = txtBirthday.getText().toString().trim();

        rg_gender = (RadioGroup) aq.id(R.id.rg_gender).getView();
        rg_gender.setOnCheckedChangeListener(onCheckChange);
    }

    private RadioGroup.OnCheckedChangeListener onCheckChange = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            int rbt = radioGroup.getCheckedRadioButtonId();
            switch (rbt) {
                case R.id.rbt_male:
                    gender = "male";
                    break;
                case R.id.rbt_female:
                    gender = "female";
                    break;
            }
        }
    };

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnBack:
                    mCallback.onBack();
                    break;
                case R.id.txtRight:
                    registerFamily();
                    break;
                case R.id.imgHeader:
                    chooseDialog();
                    break;
                case R.id.rel_Address:
                    chooseAddress();
                    break;
                case R.id.rel_Birthday:
                    showBirthdayDatePicker();
                    break;
            }
        }
    };

    //出生日期选择器
    private void showBirthdayDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context, onDateSetListener, 1970, 1, 1);
        datePickerDialog.show();
    }

    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            birthday = year + "-" + (month + 1) + "-" + day;
            txtBirthday.setText(birthday);
        }
    };

    //跳转选择地址
    private void chooseAddress() {
        mCallback.addContent(new ChooseAddressFragment());
    }

    //图片选择Dialog
    private void chooseDialog() {
        View view = mInflater.inflate(R.layout.dialog_choose_photo, null);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = null;
        switch (requestCode) {
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
        startActivityForResult(intent, MyConstant.RESULT_REQUEST_CODE);
    }

    //绑定家人
    private void registerFamily() {
        String name = aq.id(R.id.edtUserName).getText().toString().trim();
        String phone = aq.id(R.id.edtMobile).getText().toString().trim();
        String remark = aq.id(R.id.edtRemark).getText().toString().trim();
        String address = aq.id(R.id.txtAddress).getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            MyToast.rtToast(context, getString(R.string.name_cannot_empty));
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            MyToast.rtToast(context, getString(R.string.mobile_cannot_empty));
            return;
        }
//        if (TextUtils.isEmpty(address)) {
//            MyToast.rtToast(context, getString(R.string.address_cannot_empty));
//            return;
//        }

        File file = null;
        if (finalImagePath != null && finalImagePath.length() != 0) {
            file = new File(finalImagePath);
        }
        showLoading();
        ResourceTaker.familyRegistration(areaCode, phone, name, gender, remark, birthday, file, new HttpCallback<JSONObject>(context) {
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
                                showSuccessDialog();
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

    //显示注册家人成功Dialog
    private void showSuccessDialog() {
        View view = mInflater.inflate(R.layout.dialog_success, null);
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(view);

        TextView btnOK = (TextView) view.findViewById(R.id.btnOK);
        TextView txtMsg = (TextView) view.findViewById(R.id.txtMsg);
        txtMsg.setText(getString(R.string.register_family_success));
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
                mCallback.onBack();
            }
        });
        dialog.show();
    }

}
