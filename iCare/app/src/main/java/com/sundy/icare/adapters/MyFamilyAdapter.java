package com.sundy.icare.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.sundy.icare.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sundy on 16/1/19.
 */
public class MyFamilyAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List listData = new ArrayList<>();
    private final int TYPE_LIST = 1;
    private final int TYPE_GRID = 2;
    private int showType = TYPE_LIST;


    public MyFamilyAdapter() {

    }

    public MyFamilyAdapter(Context context) {
        this.context = context;
        inflater = ((Activity) context).getLayoutInflater();
    }

    public void setData(List listData) {
        this.listData = listData;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }


    @Override
    public int getCount() {
        if (listData != null)
            return listData.size();
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            switch (showType) {
                case TYPE_LIST:
                    convertView = inflater.inflate(R.layout.item_my_family_listview, null);
                    break;
                case TYPE_GRID:
                    convertView = inflater.inflate(R.layout.item_my_family_gridview, null);
                    break;
            }
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    class ViewHolder {


    }
}
