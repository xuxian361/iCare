package com.sundy.icare.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sundy.icare.R;
import com.sundy.icare.utils.MyPreference;
import com.sundy.icare.utils.MyToast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sundy on 16/4/19.
 */
public class SearchResultAdapter extends RecyclerView.Adapter {

    private final String TAG = "SearchResultAdapter";
    private Context context;
    private List list = new ArrayList();

    public SearchResultAdapter(Context context) {
        this.context = context;
    }

    public void setData(List list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_result, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            MyViewHolder mHolder = (MyViewHolder) holder;
            JSONObject item = (JSONObject) list.get(position);
            if (item != null) {
                String name = item.getString("name");
                String profileImage = item.getString("profileImage");
                mHolder.imgHeader.setImageURI(Uri.parse(profileImage));
                mHolder.txtName.setText(name);
            }

            mHolder.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View viewDialog = LayoutInflater.from(context).inflate(R.layout.dialog_input_verify_info, null);
                    final Dialog dialog = new Dialog(context, R.style.dialog);
                    dialog.setContentView(viewDialog);
                    TextView btnCancel = (TextView) viewDialog.findViewById(R.id.btnCancel);
                    TextView btnConfirm = (TextView) viewDialog.findViewById(R.id.btnConfirm);
                    final EditText edtInfo = (EditText) viewDialog.findViewById(R.id.edtInfo);
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
                            String info = edtInfo.getText().toString().trim();
                            if (TextUtils.isEmpty(info)) {
                                MyToast.rtToast(context, context.getString(R.string.verify_info_cannot_empty));
                                return;
                            }
                            SharedPreferences preferences = context.getSharedPreferences(MyPreference.Preference_User, Context.MODE_PRIVATE);
                            String memberID = preferences.getString(MyPreference.Preference_User_ID, "");
                            String sessionKey = preferences.getString(MyPreference.Preference_User_sessionKey, "");

                            MyToast.rtToast(context, context.getString(R.string.please_wait_result));
                            dialog.dismiss();
                            dialog.cancel();
                        }
                    });

                    dialog.show();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView imgHeader;
        public TextView txtName;
        public Button btnAdd;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgHeader = (SimpleDraweeView) itemView.findViewById(R.id.imgHeader);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            btnAdd = (Button) itemView.findViewById(R.id.btnAdd);
        }
    }
}
