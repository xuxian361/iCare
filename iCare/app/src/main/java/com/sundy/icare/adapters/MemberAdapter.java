package com.sundy.icare.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sundy.icare.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sundy on 16/1/19.
 */
public class MemberAdapter extends RecyclerView.Adapter {

    private final String TAG = "MemberAdapter";
    private Context context;
    private List list = new ArrayList();

    public MemberAdapter(Context context) {
        this.context = context;
    }

    public void setData(List list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_member, parent, false);
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
                String remark = item.getString("remark");

                mHolder.imgHeader.setImageURI(Uri.parse(profileImage));
                if (remark.length() == 0) {
                    mHolder.txtName.setText(name);
                } else {
                    mHolder.txtName.setText(remark);
                }
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
        public TextView txtName;


        public MyViewHolder(View itemView) {
            super(itemView);
            imgHeader = (SimpleDraweeView) itemView.findViewById(R.id.imgHeader);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
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
