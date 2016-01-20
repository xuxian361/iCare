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
 * Created by sundy on 16/1/20.
 */
public class MyOrderListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List listData = new ArrayList();

    public MyOrderListAdapter() {
    }

    public MyOrderListAdapter(Context context) {
        this.context = context;
        inflater = ((Activity) context).getLayoutInflater();
    }

    public void setData(List listData) {
        this.listData = listData;
    }

    @Override
    public int getCount() {
//        if (listData != null)
//            return listData.size();
        return 12;
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_my_order_list_view, null);
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
