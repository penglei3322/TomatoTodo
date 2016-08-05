package com.example.dllo.tomatotodo.history;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dllo.tomatotodo.R;
import com.example.dllo.tomatotodo.db.DBTools;
import com.example.dllo.tomatotodo.db.HistoryAllBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dllo on 16/7/19.
 */
public class HistoryAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<HistoryGroupBean> groupBeen;
    private HashMap<String, List<HistoryChildBean>> beanListMap;

    public HistoryAdapter(Context context) {
        this.mContext = context;
    }

    public void setBeans(List<HistoryAllBean> beans) {

        groupBeen = new ArrayList<>();
        beanListMap = new HashMap<>();

        for (int i = 0; i < beans.size(); i++) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String startTime = simpleDateFormat.format(beans.get(i).getStartTime());
            String endTime = simpleDateFormat.format(beans.get(i).getEndTime());
            String name = beans.get(i).getTomatoMsg();
            String startMonth = startTime.substring(5, 7);
            String startDay = startTime.substring(8, 10);
            String startHourAndMinute = startTime.substring(11, 16);
            String endHourAndMinute = endTime.substring(11, 16);
            int week = getDay(beans.get(i).getStartTime());

            // 上面
            HistoryGroupBean historyGroupBean = new HistoryGroupBean();

            String groupData = startMonth + "月" + startDay + "日" + " 周" + week;
            List<HistoryChildBean> childBeen;
            if (isExistData(groupData)) {
                childBeen = beanListMap.get(groupData);
            } else {

                historyGroupBean.setDate(groupData);
                groupBeen.add(historyGroupBean);
                childBeen = new ArrayList<>();
                beanListMap.put(groupData, childBeen);
            }

            // 下面
            HistoryChildBean historyChildBean = new HistoryChildBean();
            historyChildBean.setFirstTime(startTime);
            historyChildBean.setLastTime(endTime);
            historyChildBean.setName(name);

            childBeen.add(historyChildBean);
        }
        notifyDataSetChanged();
    }

    private boolean isExistData(String s) {
        for (HistoryGroupBean historyGroupBean : groupBeen) {
            if (historyGroupBean.getDate().equals(s))
                return true;
        }
        return false;
    }

    @Override
    public int getGroupCount() {
        return groupBeen == null ? 0 : groupBeen.size();
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        List<HistoryChildBean> historyChildBeen = beanListMap.get(groupBeen.get(groupPosition).getDate());
        return historyChildBeen.size();
    }

    // 获取指定组中的数据
    @Override
    public Object getGroup(int groupPosition) {
        return groupBeen.get(groupPosition);
    }

    // 获取指定组中的指定子元素数据。
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return beanListMap.get(groupBeen.get(groupPosition)).get(childPosition);
    }

    // 获取指定组中的指定子元素ID
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        DataHolder dataHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_history_list_data, parent, false);
            dataHolder = new DataHolder(convertView);
            convertView.setTag(dataHolder);
        } else {
            dataHolder = (DataHolder) convertView.getTag();
        }

        dataHolder.historyDay.setText(groupBeen.get(groupPosition).getDate());
        dataHolder.historyNumber.setText(beanListMap.get(groupBeen.get(groupPosition).getDate()).size() + "");
        return convertView;
    }


    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        TimeHolder timeHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_history_list_time, parent, false);
            timeHolder = new TimeHolder(convertView);
            convertView.setTag(timeHolder);
        } else {
            timeHolder = (TimeHolder) convertView.getTag();
        }
        timeHolder.historyName.setText(beanListMap.get(groupBeen.get(groupPosition).getDate()).get(childPosition).getName());
        timeHolder.historyFirstTime.setText(beanListMap.get(groupBeen.get(groupPosition).getDate()).get(childPosition).getFirstTime().substring(11,16));
        timeHolder.historyLastTime.setText(beanListMap.get(groupBeen.get(groupPosition).getDate()).get(childPosition).getLastTime().substring(11,16));


        final TimeHolder finalTimeHolder = timeHolder;


        timeHolder.historyDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final List<HistoryChildBean> historyChildBeens = beanListMap.get(groupBeen.get(groupPosition).getDate());
                final HistoryChildBean historyChildBean = historyChildBeens.get(childPosition);

                Snackbar.make(finalTimeHolder.historyDelete, "已删除", Snackbar.LENGTH_LONG).setAction("撤销删除", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        historyChildBeens.add(childPosition, historyChildBean);
                        delete = false;
                        //DBTools.getInstance(context).insertSingle(potatolistChildData);
                        notifyDataSetChanged();
                    }
                }).setActionTextColor(Color.RED).show();

                deleteFromDb(historyChildBean);
                historyChildBeens.remove(historyChildBean);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public boolean delete = true;

    public void deleteFromDb(final HistoryChildBean childBean) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (delete)
//                    try {
//                        long startTime = simpleDateFormat.parse(childBean.getFirstTime()).getTime();
//                        Log.d("HistoryAdapter", "startTime:" + startTime);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }

                        DBTools.getInstance(mContext).deleteCondition(HistoryAllBean.class, "tomatoMsg", childBean.getName());
                Intent intent = new Intent("refurbish");
                mContext.sendBroadcast(intent);
                delete = true;
            }
        }).start();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class DataHolder {

        TextView historyDay, historyNumber;

        public DataHolder(View view) {
            super();
            historyDay = (TextView) view.findViewById(R.id.item_history_day);
            historyNumber = (TextView) view.findViewById(R.id.item_history_number);
        }
    }

    class TimeHolder {

        TextView historyFirstTime, historyLastTime, historyName;
        ImageView historyDelete;

        public TimeHolder(View view) {
            super();
            historyFirstTime = (TextView) view.findViewById(R.id.item_history_first_time);
            historyLastTime = (TextView) view.findViewById(R.id.item_history_last_time);
            historyName = (TextView) view.findViewById(R.id.item_history_name);
            historyDelete = (ImageView) view.findViewById(R.id.item_history_delete_iv);
        }
    }

    // 由long型时间获取星期几
    public int getDay(Long startTime) {
        // 前面的lSysTime是秒数，先乘1000得到毫秒数，再转为java.util.Date类型
        Date dt = new Date(startTime);
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        return cal.get(Calendar.DAY_OF_WEEK) - 1;
    }

}
