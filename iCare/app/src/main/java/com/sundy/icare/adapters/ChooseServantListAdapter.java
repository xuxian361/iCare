package com.sundy.icare.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sundy.icare.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sundy on 16/2/1.
 */
public class ChooseServantListAdapter extends BaseAdapter {

    ViewHolder holder;
    private Context context;
    private LayoutInflater inflater;
    private List listData = new ArrayList();
    private static HashMap<Integer, Boolean> isSelected = new HashMap<Integer, Boolean>();


    public ChooseServantListAdapter(Context context) {
        this.context = context;
        this.inflater = ((Activity) context).getLayoutInflater();
        isSelected = new HashMap<Integer, Boolean>();
    }

    public void setData(List<String> listData) {
        this.listData = listData;
        for (int i = 0; i < listData.size(); i++) {
            getIsSelected().put(i, false);
        }
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void refreshSelected(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            getIsSelected().put(i, false);
        }
    }

    @Override
    public int getCount() {
        if (listData != null)
            return listData.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView,
                        ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            row = inflater.inflate(R.layout.item_choose_servant_list, null);
            holder = new ViewHolder();
            holder.btnAccept = (TextView) row.findViewById(R.id.btnAccept);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        try {
            if (getIsSelected().get(position)) {
                holder.btnAccept.setBackgroundResource(R.drawable.corner_btn_green);
            } else {
                holder.btnAccept.setBackgroundResource(R.drawable.corner_btn_gray);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return row;
    }


    public class ViewHolder {
        public TextView btnAccept;

    }

}