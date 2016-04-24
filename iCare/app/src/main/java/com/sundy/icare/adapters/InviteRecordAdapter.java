package com.sundy.icare.adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sundy.icare.R;
import com.sundy.icare.net.HttpCallback;
import com.sundy.icare.net.ResourceTaker;
import com.sundy.icare.utils.MyToast;
import com.sundy.icare.utils.MyUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sundy on 16/4/23.
 */
public class InviteRecordAdapter extends RecyclerView.Adapter {

    private final String TAG = "InviteRecordAdapter";
    private Context context;
    private List list = new ArrayList();

    public InviteRecordAdapter(Context context) {
        this.context = context;
    }

    public void setData(List list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_invite_record, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        try {
            final MyViewHolder mHolder = (MyViewHolder) holder;
            final JSONObject item = (JSONObject) list.get(position);
            if (item != null) {
                final String id = item.getString("id");
                String name = item.getString("name");
                String profileImage = item.getString("profileImage");
                boolean isOwnerApply = item.getBoolean("isOwnerApply");
                String status = item.getString("status");

                mHolder.imgHeader.setImageURI(Uri.parse(profileImage));
                mHolder.txtName.setText(name);

                if (isOwnerApply) { //邀请对方
                    mHolder.linearChoose.setVisibility(View.GONE);
                    if (status.equals("pending")) {//对方还没有回应
                        mHolder.txtTips.setVisibility(View.GONE);
                        mHolder.btnApply.setVisibility(View.GONE);
                        mHolder.txtSubName.setText(context.getString(R.string.invite_pending_tips));
                    } else if (status.equals("agree")) { //对方同意邀请
                        mHolder.txtTips.setVisibility(View.GONE);
                        mHolder.btnApply.setVisibility(View.GONE);
                        mHolder.txtSubName.setText(context.getString(R.string.invite_agree_tips));
                    } else if (status.equals("reject")) {//对方拒绝邀请
                        mHolder.txtTips.setVisibility(View.GONE);
                        mHolder.btnApply.setVisibility(View.VISIBLE);
                        mHolder.txtSubName.setText(context.getString(R.string.invite_reject_tips));
                    }
                } else {//别人发来邀请
                    if (status.equals("pending")) {//还没有对邀请回复
                        mHolder.linearChoose.setVisibility(View.VISIBLE);
                        mHolder.txtTips.setVisibility(View.GONE);
                        mHolder.btnApply.setVisibility(View.GONE);
                    } else if (status.equals("agree")) { //同意了对方的邀请
                        mHolder.linearChoose.setVisibility(View.GONE);
                        mHolder.txtTips.setVisibility(View.VISIBLE);
                        mHolder.btnApply.setVisibility(View.GONE);
                        mHolder.txtSubName.setText("二叔");
                        mHolder.txtTips.setText(context.getString(R.string.agreed));
                    } else if (status.equals("reject")) {//拒绝了对方的邀请
                        mHolder.linearChoose.setVisibility(View.GONE);
                        mHolder.txtTips.setVisibility(View.VISIBLE);
                        mHolder.btnApply.setVisibility(View.GONE);
                        mHolder.txtSubName.setText("二叔");
                        mHolder.txtTips.setText(context.getString(R.string.rejected));
                    }
                }

                mHolder.btnApply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MyUtils.rtLog(TAG, "------------>邀请");
                    }
                });
                mHolder.btnRefuse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MyUtils.rtLog(TAG, "------------>拒绝");
                        ResourceTaker.handleBind(id, "reject", new HttpCallback<JSONObject>((Activity) context) {
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
                                                item.put("status", "reject");
                                                notifyItemChanged(position);
                                                MyToast.rtToast(context, context.getString(R.string.rejected));
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
                mHolder.btnAgree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MyUtils.rtLog(TAG, "------------>同意");
                        ResourceTaker.handleBind(id, "agree", new HttpCallback<JSONObject>((Activity) context) {
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
                                                item.put("status", "agree");
                                                notifyItemChanged(position);
                                                MyToast.rtToast(context, context.getString(R.string.agreed));
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

            }
            if (onItemClickListener != null) {
                mHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = mHolder.getLayoutPosition();
                        onItemClickListener.onItemClick(holder.itemView, position);
                    }
                });
                mHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        int position = mHolder.getLayoutPosition();
                        onItemClickListener.onItemLongClick(mHolder.itemView, position);
                        return false;
                    }
                });
            }
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
        public TextView txtName, txtSubName;
        public Button btnAdd;
        public LinearLayout linearChoose;
        public TextView txtTips;
        public Button btnApply;
        public Button btnRefuse;
        public Button btnAgree;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgHeader = (SimpleDraweeView) itemView.findViewById(R.id.imgHeader);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtSubName = (TextView) itemView.findViewById(R.id.txtSubName);
            btnAdd = (Button) itemView.findViewById(R.id.btnAdd);
            linearChoose = (LinearLayout) itemView.findViewById(R.id.linearChoose);
            txtTips = (TextView) itemView.findViewById(R.id.txtTips);
            btnApply = (Button) itemView.findViewById(R.id.btnApply);
            btnRefuse = (Button) itemView.findViewById(R.id.btnRefuse);
            btnAgree = (Button) itemView.findViewById(R.id.btnAgree);
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }
}
