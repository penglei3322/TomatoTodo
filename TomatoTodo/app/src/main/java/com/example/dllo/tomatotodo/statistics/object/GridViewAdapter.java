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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * Created by dllo on 16/7/28.
 */
public class GridViewAdapter extends BaseAdapter {

    private OnOtherItemClickListener onOtherItemClickListener;

    private Context context;
    private ArrayList<HistoryAllBean> beanArrayList;
    private ArrayList<Integer> numPerTimeList;
    private int num;
    private CustomGridViewPoint customGridViewPoint;

    private boolean isFirst = true;

    private int mTimes;

    private int key;
    private int mType;

    public ArrayList<Integer> getNumPerTimeList() {
        return numPerTimeList;
    }

    public void setOnOtherItemClickListener(OnOtherItemClickListener onOtherItemClickListener) {
        this.onOtherItemClickListener = onOtherItemClickListener;
    }

    public GridViewAdapter(Context context) {
        numPerTimeList = new ArrayList<>();
        this.context = context;
    }

    public void setBeanArrayList(ArrayList<HistoryAllBean> beanArrayList, int type) {
        this.beanArrayList = beanArrayList;
        mType = type;
        switch (type) {
            case 1:
                mTimes = 30;

                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
                for (int i = 0; i < mTimes; i++) {
                    for (HistoryAllBean bean : beanArrayList) {
                        if (sdf.format(bean.getStartTime()).equals(sdf.format(getNextDayThirtyDay(new Date(), mTimes - i)))) {
                            num++;
                        }
                    }
                    numPerTimeList.add(num);
                    num = 0;
                }

                break;
            case 2:
                mTimes = 12;
                for (int i = 0; i < mTimes; i++) {
                    long weekStart = getCurWeekStartTime() - 7 * (mTimes - 1 - i) * 24 * 60 * 60 * 1000l;
                    long weekEnd = getCurWeekStartTime() - 7 * (mTimes - 2 - i) * 24 * 60 * 60 * 1000l;
                    for (HistoryAllBean bean : beanArrayList) {
                        if (bean.getStartTime() > weekStart &&
                                bean.getStartTime() < weekEnd) {
                            num++;
                        }
                    }
                    numPerTimeList.add(num);
                    num = 0;
                }
                break;
            case 3:
                mTimes = 12;
                for (int i = 0; i < mTimes; i++) {
                    long monthStart = getStartTimeOfMonth(i);
                    long monthEnd = getStartTimeOfMonth(i + 1);
                    for (HistoryAllBean bean : beanArrayList) {
                        if (bean.getStartTime() > monthStart && bean.getStartTime() < monthEnd){
                            num++;
                        }
                    }
                    numPerTimeList.add(num);
                    num = 0;
                }
                break;
        }


//        for (int i = 0; i < mTimes; i++) {
//            hashMap.put(i, 0);
//        }


//        for (HistoryAllBean bean : beanArrayList) {
//            if (type == 1){
//                key = (int) (mTimes - (System.currentTimeMillis() - bean.getStartTime()) / (24 * 60 * 60 * 1000)) - 1;
//            } else if (type == 2){
//                key = (int) (mTimes - (System.currentTimeMillis() - bean.getStartTime()) / (7 * 24 * 60 * 60 * 1000)) - 1;
//            } else {
//                key = (int) (mTimes - (System.currentTimeMillis() - bean.getStartTime()) / (7 * 24 * 60 * 60 * 1000)) - 1;
//            }
//            if (hashMap.containsKey(key)) {
//                hashMap.put(key, hashMap.get(key) + 1);
//            } else {
//                hashMap.put(key, 1);
//            }
//        }

        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
//        if (mType == 1){
//            return 30;
//        } else {
//            return 12;
//        }
        return numPerTimeList.size();
    }

    @Override
    public Object getItem(int position) {
//        if (hashMap.containsKey(position)) {
//            return hashMap.get(position);
//        } else {
//            return 0;
//        }
        return numPerTimeList.get(position);
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

        holder.customGridViewPoint.setProgress(numPerTimeList.get(position), mType);

        holder.customGridViewPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((CustomGridViewPoint) v != customGridViewPoint && !isFirst) {
                    onOtherItemClickListener.onOtherItemClick(customGridViewPoint);
                }
                isFirst = false;
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


    /**
     * 返回过去30天每天的日期
     *
     * @param date
     * @param i
     * @return
     */
    public Date getNextDayThirtyDay(Date date, int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -i + 1);
        date = calendar.getTime();
        return date;
    }

    // 获取本周开始时间
    public long getCurWeekStartTime() {
        Calendar currentDate = new GregorianCalendar();
        currentDate.setFirstDayOfWeek(Calendar.MONDAY);
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return currentDate.getTime().getTime();
    }

    // 获取过去i个月开始的时间
    public long getStartTimeOfMonth(int i){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1); //要先+1,才能把本月的算进去
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - (12 - i));
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime().getTime();
    }

}
