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
public class GridViewAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<HistoryAllBean> beanArrayList;
    private HashMap<Integer, Integer> hashMap;
    private boolean isFirst = true;

    public GridViewAdapter(Context context) {
        hashMap = new HashMap<>();
        this.context = context;
    }

    public void setBeanArrayList(ArrayList<HistoryAllBean> beanArrayList) {
        this.beanArrayList = beanArrayList;

        for (HistoryAllBean bean : beanArrayList) {
            int key = (int) (30 - (System.currentTimeMillis() - bean.getStartTime()) / (24*60*60*1000));
            if (hashMap.containsKey(key)){
                hashMap.put(key,hashMap.get(key) + 1);
            } else {
                hashMap.put(key,1);
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return 30;
    }

    @Override
    public Object getItem(int position) {
        if (hashMap.containsKey(position)){
            Log.d("GridViewAdapter", "hashMap.get(position):" + hashMap.get(position));
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_grid_view,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.customGridViewPoint.setProgress(hashMap.get(position));
        Log.d("GridViewAdapter", "hashMap.get(position):" + hashMap.get(position));

        holder.customGridViewPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

}
