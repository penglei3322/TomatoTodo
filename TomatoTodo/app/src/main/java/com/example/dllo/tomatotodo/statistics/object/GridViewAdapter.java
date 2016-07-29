package com.example.dllo.tomatotodo.statistics.object;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.dllo.tomatotodo.R;
import com.example.dllo.tomatotodo.custom.CustomGridViewPoint;
import com.example.dllo.tomatotodo.db.HistoryAllBean;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dllo on 16/7/28.
 */
public class GridViewAdapter extends BaseAdapter {

    private OnOtherItemClickListener onOtherItemClickListener;

    private Context context;
    private ArrayList<HistoryAllBean> beanArrayList;
    private HashMap<Integer, Integer> hashMap;
    private CustomGridViewPoint customGridViewPoint;

    private int mTimes;
    private int key;
    private int mType;

    public void setOnOtherItemClickListener(OnOtherItemClickListener onOtherItemClickListener) {
        this.onOtherItemClickListener = onOtherItemClickListener;
    }

    public GridViewAdapter(Context context) {
        hashMap = new HashMap<>();
        this.context = context;
    }

    public void setBeanArrayList(ArrayList<HistoryAllBean> beanArrayList,int type) {
        this.beanArrayList = beanArrayList;
        mType = type;
        switch (type){
            case 1:
                mTimes = 30;
                break;
            case 2:
                mTimes = 12;
                break;
            case 3:
                mTimes = 12;
                break;
        }
        for (int i = 0; i < mTimes; i++) {
            hashMap.put(i, 0);
        }

        for (HistoryAllBean bean : beanArrayList) {
            if (type == 1){
                key = (int) (mTimes - (System.currentTimeMillis() - bean.getStartTime()) / (24 * 60 * 60 * 1000)) - 1;
            } else if (type == 2){
                key = (int) (mTimes - (System.currentTimeMillis() - bean.getStartTime()) / (7 * 24 * 60 * 60 * 1000)) - 1;
            } else {
                key = (int) (mTimes - (System.currentTimeMillis() - bean.getStartTime()) / (7 * 24 * 60 * 60 * 1000)) - 1;
            }
            if (hashMap.containsKey(key)) {
                hashMap.put(key, hashMap.get(key) + 1);
            } else {
                hashMap.put(key, 1);
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mType == 1){
            return 30;
        } else {
            return 12;
        }
    }

    @Override
    public Object getItem(int position) {
        if (hashMap.containsKey(position)) {
            return hashMap.get(position);
        } else {
            return 0;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_grid_view, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.customGridViewPoint.setProgress(hashMap.get(position));

        holder.customGridViewPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((CustomGridViewPoint) v != customGridViewPoint) {
                    onOtherItemClickListener.onOtherItemClick(customGridViewPoint);
                }
                customGridViewPoint = (CustomGridViewPoint) v;
            }
        });

        return convertView;
    }


    class ViewHolder {

        CustomGridViewPoint customGridViewPoint;

        public ViewHolder(View view) {
            customGridViewPoint = (CustomGridViewPoint) view.findViewById(R.id.item_point);
        }
    }

    interface OnOtherItemClickListener {
        void onOtherItemClick(CustomGridViewPoint customGridViewPoint);
    }

}
