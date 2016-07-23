package com.example.dllo.tomatotodo.history;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dllo.tomatotodo.R;

import java.util.ArrayList;

/**
 * Created by dllo on 16/7/19.
 */
public class HistoryAdapter extends BaseAdapter {

    private ArrayList<HistoryBean> historyBeen;
    private Context context;
    private LayoutInflater inflater;
    private static final int TYPE_DATA = 0;
    private static final int TYPE_TIME = 1;

    public HistoryAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setHistoryBeen(ArrayList<HistoryBean> historyBeen) {
        this.historyBeen = historyBeen;
        Log.d("Clark", "historyBeen:" + historyBeen);
        notifyDataSetChanged();
    }

    private int getCountByGroup(int groupPos){

        return 0;
    }

    private boolean ifHasData() {
        return historyBeen.size() > 0 && historyBeen != null;
    }

    @Override
    public int getCount() {
        return ifHasData() ? historyBeen.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return ifHasData() ? historyBeen.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 根据position位置获取数据集合的数据
     * 返回行布局类型
     */
    @Override
    public int getItemViewType(int position) {
        String type = historyBeen.get(position).getType();
        if ("0".equals(type)) {
            return TYPE_DATA;
        } else {
            return TYPE_TIME;
        }
    }

    /**
     * 行布局类型总数
     */
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DataHolder dataHolder = null;
        TimeHolder timeHolder = null;
        int type = getItemViewType(position);

        // 加载行布局+缓存行布局组件
        if (convertView == null) {
            switch (type) {
                case TYPE_DATA:
                    convertView = inflater.inflate(R.layout.item_history_list_data, parent, false);
                    dataHolder = new DataHolder(convertView);
                    convertView.setTag(dataHolder);
                    break;
                case TYPE_TIME:
                    convertView = inflater.inflate(R.layout.item_history_list_time, parent, false);
                    timeHolder = new TimeHolder(convertView);
                    convertView.setTag(timeHolder);
                    break;
            }
        } else {
            switch (type) {
                case TYPE_DATA:
                    dataHolder = (DataHolder) convertView.getTag();
                    break;
                case TYPE_TIME:
                    timeHolder = (TimeHolder) convertView.getTag();
                    break;
            }
        }

        // 设置行布局组件内容
        HistoryBean historyBean = (HistoryBean) getItem(position);
        switch (type) {
            case TYPE_DATA:
                dataHolder.historyData.setText(historyBean.getHistoryData());
                dataHolder.historyWeeks.setText(historyBean.getHistoryWeeks());
                dataHolder.historyNumber.setText(historyBean.getHistoryNumber());
                break;
            case TYPE_TIME:
                timeHolder.historyFirstTime.setText(historyBean.getHistoryFirstTime());
                timeHolder.historyLastTime.setText(historyBean.getHistoryLastTime());
                timeHolder.historyName.setText(historyBean.getHistoryName());
                break;
        }
        return convertView;
    }

    class DataHolder {

        TextView historyData, historyWeeks, historyNumber;

        public DataHolder(View view) {
            super();
            historyData = (TextView) view.findViewById(R.id.item_history_data);
            historyWeeks = (TextView) view.findViewById(R.id.item_history_weeks);
            historyNumber = (TextView) view.findViewById(R.id.item_history_number);
        }
    }

    class TimeHolder {

        TextView historyFirstTime, historyLastTime, historyName;

        public TimeHolder(View view) {
            super();
            historyFirstTime = (TextView) view.findViewById(R.id.item_history_first_time);
            historyLastTime = (TextView) view.findViewById(R.id.item_history_last_time);
            historyName = (TextView) view.findViewById(R.id.item_history_name);
        }


    }

}
