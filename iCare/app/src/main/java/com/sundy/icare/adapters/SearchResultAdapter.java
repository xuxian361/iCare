package com.sundy.icare.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sundy.icare.R;
import com.sundy.icare.utils.MyUtils;

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        MyUtils.rtLog(TAG, "------count = " + list.size());
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
