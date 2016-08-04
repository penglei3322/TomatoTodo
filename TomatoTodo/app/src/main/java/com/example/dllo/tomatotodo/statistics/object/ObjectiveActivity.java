package com.example.dllo.tomatotodo.statistics.object;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dllo.tomatotodo.R;
import com.example.dllo.tomatotodo.base.BaseActivity;
import com.example.dllo.tomatotodo.base.MyApp;
import com.example.dllo.tomatotodo.custom.CustomGridViewPoint;
import com.example.dllo.tomatotodo.db.DBTools;
import com.example.dllo.tomatotodo.db.HistoryAllBean;
import com.example.dllo.tomatotodo.statistics.ObjectOptionsActivity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by dllo on 16/7/19.
 */
public class ObjectiveActivity extends BaseActivity {

    private ArrayList<HistoryAllBean> monthAgoList;
    private ArrayList<HistoryAllBean> lastTwelveDataList;
    private ArrayList<HistoryAllBean> lastTwelveMonthList;
    private ArrayList<HistoryAllBean> curDayList;
    private ArrayList<HistoryAllBean> curWeekList;
    private ArrayList<HistoryAllBean> curMonthList;


    private GridView mGridViewLastMonth, mGridViewTwelveWeek, mGridViewTwelveMonth;
    private GridViewAdapter adapterLastMonth, adapterLastTwelveWeek, adapterLastTwelveMonth;

    private ProgressBar mLastMonthProgressBar, mLastTwelveWeekProgressBar, mLastTwelveMonthProgressBar;

    private TextView lastMonthComplete, lastMonthAva, lastMonthRate;
    private TextView lastTwelveWeekComplete, lastTwelveWeekAva, lastTwelveWeekRate;
    private TextView lastTwelveMonthComplete, lastTwelveMonthAva, lastTwelveMonthRate;
    private TextView mCurDayProgress, mCurWeekProgress, mCurMonthProgress;

    private TextView tvReturn, tvOptions;

    private int objectDay, objectWeek, objectMonth;

    private TextView mEveryDayObject, mEveryWeekObject, mEveryMonthObject;

    private objectReceiver receiver;

    @Override
    public int initView() {
        return R.layout.activity_object;
    }

    @Override
    public void initData() {
        mLastMonthProgressBar = (ProgressBar) findViewById(R.id.progressBarLastMonth);
        mLastTwelveWeekProgressBar = (ProgressBar) findViewById(R.id.progressBarLastTwelveWeek);
        mLastTwelveMonthProgressBar = (ProgressBar) findViewById(R.id.progressBarLastTwelveMonth);
        mGridViewLastMonth = (GridView) findViewById(R.id.gridViewLastMonth);
        mGridViewTwelveWeek = (GridView) findViewById(R.id.gridViewLastTwelveWeek);
        mGridViewTwelveMonth = (GridView) findViewById(R.id.gridViewTwelveMonth);

        lastMonthComplete = (TextView) findViewById(R.id.tv_object_last_month_complete);
        lastMonthAva = (TextView) findViewById(R.id.tv_object_average_last_month_average);
        lastMonthRate = (TextView) findViewById(R.id.tv_complete_last_mouth_yield_rate);
        mCurDayProgress = (TextView) findViewById(R.id.curDayProgress);

        lastTwelveWeekComplete = (TextView) findViewById(R.id.tv_object_twelve_week_complete);
        lastTwelveWeekAva = (TextView) findViewById(R.id.tv_object_twelve_week_average);
        lastTwelveWeekRate = (TextView) findViewById(R.id.tv_object_twelve_week_yield_rate);
        mCurWeekProgress = (TextView) findViewById(R.id.curWeekProgress);

        lastTwelveMonthComplete = (TextView) findViewById(R.id.tv_object_twelve_month_complete);
        lastTwelveMonthAva = (TextView) findViewById(R.id.tv_object_twelve_month_average);
        lastTwelveMonthRate = (TextView) findViewById(R.id.tv_last_twelve_month_yield_rate);
        mCurMonthProgress = (TextView) findViewById(R.id.curMonthProgress);

        tvReturn = (TextView) findViewById(R.id.tv_object_return);
        tvOptions = (TextView) findViewById(R.id.tv_object_options);

        mEveryDayObject = (TextView) findViewById(R.id.tv_object_every_day);
        mEveryWeekObject = (TextView) findViewById(R.id.tv_object_every_week);
        mEveryMonthObject = (TextView) findViewById(R.id.tv_object_every_month);

        mGridViewLastMonth.setVerticalSpacing(30);
        mGridViewTwelveWeek.setVerticalSpacing(30);
        mGridViewTwelveMonth.setVerticalSpacing(30);
        adapterLastMonth = new GridViewAdapter(this);
        adapterLastTwelveWeek = new GridViewAdapter(this);
        adapterLastTwelveMonth = new GridViewAdapter(this);

        receiver = new objectReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("refresh");
        registerReceiver(receiver,filter);

        //获取sp
        getSp();



        tvReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyApp.context, ObjectOptionsActivity.class);
                startActivity(intent);
            }
        });


        adapterLastMonth.setOnOtherItemClickListener(new GridViewAdapter.OnOtherItemClickListener() {
            @Override
            public void onOtherItemClick(CustomGridViewPoint customGridViewPoint) {
                customGridViewPoint.setTouch(false);
            }
        });

        adapterLastTwelveWeek.setOnOtherItemClickListener(new GridViewAdapter.OnOtherItemClickListener() {
            @Override
            public void onOtherItemClick(CustomGridViewPoint customGridViewPoint) {
                customGridViewPoint.setTouch(false);
            }
        });

        adapterLastTwelveMonth.setOnOtherItemClickListener(new GridViewAdapter.OnOtherItemClickListener() {
            @Override
            public void onOtherItemClick(CustomGridViewPoint customGridViewPoint) {
                customGridViewPoint.setTouch(false);
            }
        });

        getLastThirtyData(); // 过去30天
        getLastTwelveWeekData(); // 过去12周
        getLastTwelveMonthData(); // 过去12个月

        getCurrentDayData(); // 获取本周数据
        getCurrentWeekData(); // 获取本月数据
        getCurrentMonthData(); // 获取本月数据

    }

    //获取存好的SharedPreferences
    private void getSp() {

        SharedPreferences sp = getSharedPreferences("object", MODE_PRIVATE);
        objectDay = sp.getInt("objectDay", 8);
        objectWeek = sp.getInt("objectWeek", 40);
        objectMonth = sp.getInt("objectMonth", 160);

        mEveryDayObject.setText("每日目标: "+objectDay+"");
        mEveryWeekObject.setText("每周目标: "+objectWeek+"");
        mEveryMonthObject.setText("每月目标: "+objectMonth+"");

    }

    // 获取过去30天的数据并设置到gridView上
    public void getLastThirtyData() {
        //30天前 获取30天前的日期(Java)
        Calendar theCa = Calendar.getInstance();
        theCa.setTime(new Date());
        theCa.add(theCa.DATE, -30);
        Date date = theCa.getTime();
        //转型
        long mouthAgo = date.getTime();
        //根据时间遍历出所有符合条件的数据
        monthAgoList = new ArrayList();
        for (HistoryAllBean historyAllBean : DBTools.getInstance(this).queryAll(HistoryAllBean.class)) {
            if (historyAllBean.getStartTime() > mouthAgo) {
                monthAgoList.add(historyAllBean);
            }
        }
        adapterLastMonth.setBeanArrayList(monthAgoList, 1);
        mGridViewLastMonth.setAdapter(adapterLastMonth);

        DecimalFormat format = new DecimalFormat("##0.00");
        lastMonthComplete.setText(monthAgoList.size() + "");
        lastMonthAva.setText(format.format(monthAgoList.size() / 30f) + "");
        int num = 0;
        for (Integer integer : adapterLastMonth.getNumPerTimeList()) {
            if (integer >= objectDay) {
                num++;
            }
        }
        lastMonthRate.setText(format.format(num / 30f * 100) + "%");

    }

    // 获取过去12周的数据并设置到gridView上
    public void getLastTwelveWeekData() {
        lastTwelveDataList = new ArrayList<>();
        for (HistoryAllBean historyAllBean : DBTools.getInstance(this).queryAll(HistoryAllBean.class)) {
            if (historyAllBean.getStartTime() > getCurWeekStartTime() + 11 * 7 * 24 * 60 * 60 * 1000) {
                lastTwelveDataList.add(historyAllBean);
            }
        }
        adapterLastTwelveWeek.setBeanArrayList(lastTwelveDataList, 2);
        mGridViewTwelveWeek.setAdapter(adapterLastTwelveWeek);

        DecimalFormat format = new DecimalFormat("##0.00");
        lastTwelveWeekComplete.setText(lastTwelveDataList.size() + "");
        lastTwelveWeekAva.setText(format.format(lastTwelveDataList.size() / 12f) + "");
        int num = 0;
        for (Integer integer : adapterLastTwelveWeek.getNumPerTimeList()) {
            if (integer >= objectWeek) {
                num++;
            }
        }
        lastTwelveWeekRate.setText(format.format(num / 12f * 100) + "%");
    }


    // 获取过去12个月的数据并设置到gridView上
    public void getLastTwelveMonthData() {
        lastTwelveMonthList = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1); //要先+1,才能把本月的算进去
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 12);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        for (HistoryAllBean historyAllBean : DBTools.getInstance(this).queryAll(HistoryAllBean.class)) {
            if (historyAllBean.getStartTime() > cal.getTime().getTime()) {
                lastTwelveMonthList.add(historyAllBean);
            }
        }
        adapterLastTwelveMonth.setBeanArrayList(lastTwelveMonthList, 3);
        mGridViewTwelveMonth.setAdapter(adapterLastTwelveMonth);

        DecimalFormat format = new DecimalFormat("##0.00");
        lastTwelveMonthComplete.setText(lastTwelveMonthList.size() + "");
        lastTwelveMonthAva.setText(format.format(lastTwelveMonthList.size() / 12f) + "");
        int num = 0;
        for (Integer integer : adapterLastTwelveMonth.getNumPerTimeList()) {
            if (integer >= objectMonth) {
                num++;
            }
        }
        lastTwelveMonthRate.setText(format.format(num / 12f * 100) + "%");

    }

    // 获取当日数据
    public void getCurrentDayData() {
        curDayList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        for (HistoryAllBean historyAllBean : DBTools.getInstance(this).queryAll(HistoryAllBean.class)) {
            if (simpleDateFormat.format(historyAllBean.getStartTime()).equals(simpleDateFormat.format(new Date()))) {
                curDayList.add(historyAllBean);
            }
        }
        mLastMonthProgressBar.setMax(objectDay);
        mLastMonthProgressBar.setProgress(curDayList.size());
        mCurDayProgress.setText(curDayList.size() + "/" + objectDay);
    }

    // 获取本周数据
    public void getCurrentWeekData() {
        curWeekList = new ArrayList<>();
        for (HistoryAllBean historyAllBean : DBTools.getInstance(this).queryAll(HistoryAllBean.class)) {
            if (historyAllBean.getStartTime() > getCurWeekStartTime()) {
                curWeekList.add(historyAllBean);
            }
        }
        mLastTwelveWeekProgressBar.setMax(objectWeek);
        mLastTwelveWeekProgressBar.setProgress(curWeekList.size());
        mCurWeekProgress.setText(curWeekList.size() + "/" + objectWeek);
    }

    // 获取本月数据
    public void getCurrentMonthData() {
        curMonthList = new ArrayList<>();
        for (HistoryAllBean historyAllBean : DBTools.getInstance(this).queryAll(HistoryAllBean.class)) {
            if (historyAllBean.getStartTime() > getCurMonthStartTime()) {
                curMonthList.add(historyAllBean);
            }
        }
        mLastTwelveMonthProgressBar.setMax(objectMonth);
        mLastTwelveMonthProgressBar.setProgress(curMonthList.size());
        mCurMonthProgress.setText(curMonthList.size() + "/" + objectMonth);
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

    // 获取当月开始的时间
    public long getCurMonthStartTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1); //要先+1,才能把本月的算进去
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime().getTime();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    class objectReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            //调取方法刷新界面
            getSp();
        }
    }


}
