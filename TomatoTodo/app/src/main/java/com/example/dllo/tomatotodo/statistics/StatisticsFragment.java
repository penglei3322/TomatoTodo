package com.example.dllo.tomatotodo.statistics;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.dllo.tomatotodo.R;
import com.example.dllo.tomatotodo.base.BaseFragment;
import com.example.dllo.tomatotodo.base.MyApp;
import com.example.dllo.tomatotodo.custom.CustomClock;
import com.example.dllo.tomatotodo.custom.CustomHistogramView;
import com.example.dllo.tomatotodo.custom.CustomLineChartView;
import com.example.dllo.tomatotodo.db.DBTools;
import com.example.dllo.tomatotodo.db.HistoryAllBean;
import com.example.dllo.tomatotodo.statistics.object.ObjectiveActivity;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by dllo on 16/7/18.
 */
public class StatisticsFragment extends BaseFragment implements View.OnClickListener {

    private PullToRefreshScrollView scrollView;
    private TextView tvLeft, tvCenter, tvRight, tvAll, tvAverage, tvGrowth;
    private HashMap<Integer, String> map;
    private String date;//当时的日期,用作点击事件判断
    private int dates;//map里日期的key
    private int temp = 0;//记录点击的次数
    private ArrayList<HistoryAllBean> mouthAgoList;

    SimpleDateFormat format = new SimpleDateFormat("MM");
    SimpleDateFormat formatEveryDate = new SimpleDateFormat("yyyyMM");
    private int num = 1;
    private CustomLineChartView lineChartView;

    private HashMap<Integer, Integer> hashHistogram;
    private CustomHistogramView customHistogramView;
    private CustomClock customClock;

    @Override
    public int createView() {
        return R.layout.fragmet_statistics;
    }

    @Override
    public void initView(View view) {
        tvLeft = (TextView) view.findViewById(R.id.tv_left_fragment_statics);
        tvCenter = (TextView) view.findViewById(R.id.tv_center_fragment_statics);
        tvRight = (TextView) view.findViewById(R.id.tv_right_fragment_statics);
        tvAll = (TextView) view.findViewById(R.id.tv_tomato_all);
        tvAverage = (TextView) view.findViewById(R.id.tv_daily_average);
        tvGrowth = (TextView) view.findViewById(R.id.tv_month_growth);
        scrollView = (PullToRefreshScrollView) view.findViewById(R.id.scrollViewStatics);
        customHistogramView = (CustomHistogramView) view.findViewById(R.id.customHistogramView);
        lineChartView = (CustomLineChartView) view.findViewById(R.id.customLineChartView);
        customClock = (CustomClock) view.findViewById(R.id.customClock);
        tvLeft.setOnClickListener(this);
        tvCenter.setOnClickListener(this);
        tvRight.setOnClickListener(this);

        scrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        scrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                Intent intent = new Intent(MyApp.context,ObjectiveActivity.class);
                startActivity(intent);
                scrollView.onRefreshComplete();
            }
        });

    }

    @Override
    public void initData() {
        hashHistogram = new HashMap<>();
        //添加HashMap
        tvCenter.setText("过去三十天");
        map = new HashMap<>();
        map.put(1, "一月");
        map.put(2, "二月");
        map.put(3, "三月");
        map.put(4, "四月");
        map.put(5, "五月");
        map.put(6, "六月");
        map.put(7, "七月");
        map.put(8, "八月");
        map.put(9, "九月");
        map.put(10, "十月");
        map.put(11, "十一月");
        map.put(12, "十二月");
        //根据当时时间的月份从hashMap中获取三个TextView的显示内容

        date = format.format(new Date());
        Log.d("date", date);
        //将String类型的date 转化成为int类型
        dates = Integer.valueOf(date);
        Log.d("dates", dates + "```");
        //根据转换的int类型key值拿到想要的月份
        tvLeft.setText(map.get(dates));
        //设置默认的折线图数据
        setMouthAgoData();


    }

    private void setMouthAgoData() {

        //30天前 获取30天前的日期(Java)
        Calendar theCa = Calendar.getInstance();
        theCa.setTime(new Date());
        theCa.add(theCa.DATE, -30);
        Date date = theCa.getTime();
        //转型
        long mouthAgo = date.getTime();
//        Log.d("StatisticsFragment", "mouthAgo:" + mouthAgo);
        //根据时间遍历出所有符合条件的数据
        mouthAgoList = new ArrayList();
        for (HistoryAllBean historyAllBean : DBTools.getInstance(context).queryAll(HistoryAllBean.class)) {
            if (historyAllBean.getStartTime() > mouthAgo) {
                mouthAgoList.add(historyAllBean);
            }

        }
        customClock.setBeanArrayList(mouthAgoList);
        //添加到自定义折线图
        lineChartView.setMouthAgoList(mouthAgoList, "13");
        //最近30天总数
        tvAll.setText(mouthAgoList.size() + "");
        DecimalFormat fnum = new DecimalFormat("##0.00");
        String dd = fnum.format(mouthAgoList.size() / 30F);

        //日平均数
        tvAverage.setText(dd);

        //月增长数
        tvGrowth.setText(dd);
        setHashHistogram();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left_fragment_statics:
                dates--;
                temp++;
                tvRight.setVisibility(View.VISIBLE);
                //当点击到一月份时候,让TextView可以重新循环整个map
                if (dates == 0) {
                    dates = 12;
                }
                tvRight.setText(tvCenter.getText().toString());
                //当点击左侧按钮时,把左侧按钮中的值赋给中间按钮
                tvCenter.setText(tvLeft.getText().toString());
                //根据key判断
                int keyLeft = dates % 12;
                if (keyLeft != 0) {
                    tvLeft.setText(map.get(dates % 12));
                } else {
                    tvLeft.setText(map.get(12));
                }
                //每当点击一次左侧按钮,记录一次
                setMouthData(temp);
                break;

            case R.id.tv_center_fragment_statics:

                break;

            case R.id.tv_right_fragment_statics:
                temp--;
                dates++;
                //首先判断点击的temp次数,如果为0,则拦截点击事件
                if (temp == 0) {
                    tvRight.setVisibility(View.INVISIBLE);
                    tvCenter.setText("过去三十天");
                    setMouthAgoData();
                    String month = format.format(new Date());
                    int num = Integer.valueOf(month);
                    tvLeft.setText(map.get(num));
                    int keyTemp = dates % 12;
                    if (keyTemp != 0) {
                        tvLeft.setText(map.get(keyTemp));
                    } else {
                        tvLeft.setText(map.get(12));
                    }
                    return;
                }
                if (temp == 1) {
                    tvLeft.setText(tvCenter.getText().toString());
                    tvCenter.setText(tvRight.getText().toString());
                    tvRight.setText("过去三十天");
                } else if (temp == 2) {
                    String month = format.format(new Date());
                    int num = Integer.valueOf(month);
                    tvRight.setText(map.get(num));
                    tvCenter.setText(map.get(num - 1));
                    tvLeft.setText(map.get(num - 2));

                } else {
                    tvLeft.setText(tvCenter.getText().toString());
                    tvCenter.setText(tvRight.getText().toString());
                    int keyRight = (dates + 2) % 12;
                    if (keyRight != 0) {
                        tvRight.setText(map.get(keyRight));
                    } else {
                        tvRight.setText(map.get(12));
                    }
                }
                setNextMonthDate(temp);
                break;


        }
    }

    private void setNextMonthDate(int temp) {
        mouthAgoList = new ArrayList();
        ArrayList<HistoryAllBean> realNextMonthList = new ArrayList<>();
        Log.d("StatisticsFragment", "temp:" + temp);
        for (HistoryAllBean historyAllBean : DBTools.getInstance(context).queryAll(HistoryAllBean.class)) {
            if (getLastMonth(temp).equals(formatEveryDate.format(historyAllBean.getStartTime()))) {
                mouthAgoList.add(historyAllBean);
            }
            if (getLastMonth(temp - 1).equals(formatEveryDate.format(historyAllBean.getStartTime()))) {
                mouthAgoList.add(historyAllBean);
            }

        }
        //添加饼状图
        customClock.setBeanArrayList(mouthAgoList);

        lineChartView.setMouthAgoList(mouthAgoList, String.valueOf(getLastMonth(temp)));

        String thisMonth = getLastMonth(temp);
        int yearThis = Integer.valueOf(thisMonth.substring(0, 4));
        int monthThis = Integer.valueOf(thisMonth.substring(4));
        //获取当前月的天数
        int thisMonthDays = getDaysByYearMonth(yearThis, monthThis);


        //获取之前一个月的年份月份
        String thisMonthAgo = getLastMonth(temp - 1);
        int yearAgo = Integer.valueOf(thisMonthAgo.substring(0, 4));
        int monthAgo = Integer.valueOf(thisMonthAgo.substring(4));
        //获取之前一个月的天数
        int lastMonthDays = getDaysByYearMonth(yearAgo, monthAgo);


        DecimalFormat fnum = new DecimalFormat("##0.00");

        String dayAverage = fnum.format(mouthAgoList.size() / (float) thisMonthDays);
        tvAll.setText(mouthAgoList.size() + "");
        //日平均数
        tvAverage.setText(dayAverage);

        float growth = mouthAgoList.size() / (float) thisMonthDays - realNextMonthList.size() / (float) lastMonthDays;

        if (growth < 0) {
            tvGrowth.setTextColor(Color.RED);
        } else {
            tvGrowth.setTextColor(Color.GREEN);
        }

        //月增长数
        tvGrowth.setText(fnum.format(growth));
        setHashHistogram();
    }

    //获取每个月份有多少天,并且添加到自定义折线图和柱状图
    private void setMouthData(int temp) {
        mouthAgoList = new ArrayList();
        ArrayList<HistoryAllBean> realMonthAgoList = new ArrayList<>();

        for (HistoryAllBean historyAllBean : DBTools.getInstance(context).queryAll(HistoryAllBean.class)) {
            if (getLastMonth(temp).equals(formatEveryDate.format(historyAllBean.getStartTime()))) {
                mouthAgoList.add(historyAllBean);
            }
            if (getLastMonth(temp + 1).equals(formatEveryDate.format(historyAllBean.getStartTime()))) {
                realMonthAgoList.add(historyAllBean);
            }
        }
        //添加饼状图
        customClock.setBeanArrayList(mouthAgoList);
        //添加折线图
        lineChartView.setMouthAgoList(mouthAgoList, String.valueOf(getLastMonth(temp)));

        String thisMonth = getLastMonth(temp);
        int yearThis = Integer.valueOf(thisMonth.substring(0, 4));
        int monthThis = Integer.valueOf(thisMonth.substring(4));
        //获取当前月的天数
        int thisMonthDays = getDaysByYearMonth(yearThis, monthThis);


        //获取之前一个月的年份月份
        String thisMonthAgo = getLastMonth(temp + 1);
        int yearAgo = Integer.valueOf(thisMonthAgo.substring(0, 4));
        int monthAgo = Integer.valueOf(thisMonthAgo.substring(4));
        //获取之前一个月的天数
        int lastMonthDays = getDaysByYearMonth(yearAgo, monthAgo);


        DecimalFormat fnum = new DecimalFormat("##0.00");

        String dayAverage = fnum.format(mouthAgoList.size() / (float) thisMonthDays);
        tvAll.setText(mouthAgoList.size() + "");
        //日平均数
        tvAverage.setText(dayAverage);

        float growth = mouthAgoList.size() / (float) thisMonthDays - realMonthAgoList.size() / (float) lastMonthDays;

        if (growth < 0) {
            tvGrowth.setTextColor(Color.RED);
        } else {
            tvGrowth.setTextColor(Color.GREEN);
        }

        //月增长数
        tvGrowth.setText(fnum.format(growth));

        setHashHistogram();

    }

    //判断星期后加入到对应的HashMap中
    public void setHashHistogram() {
        hashHistogram = new HashMap<>();
        int sundayNum = 0;
        int mondayNum = 0;
        int tuesdayNum = 0;
        int wednesdayNum = 0;
        int thursdayNum = 0;
        int fridayNum = 0;
        int satdayNum = 0;
        for (HistoryAllBean historyAllBean : mouthAgoList) {
            switch (getDay(historyAllBean.getStartTime())) {
                case 0:
                    sundayNum++;
                    hashHistogram.put(0, sundayNum);
                    break;
                case 1:
                    mondayNum++;
                    hashHistogram.put(1, mondayNum);
                    break;
                case 2:
                    tuesdayNum++;
                    hashHistogram.put(2, tuesdayNum);
                    break;

                case 3:
                    wednesdayNum++;
                    hashHistogram.put(3, wednesdayNum);
                    break;

                case 4:
                    thursdayNum++;
                    hashHistogram.put(4, thursdayNum);
                    break;

                case 5:
                    fridayNum++;
                    hashHistogram.put(5, fridayNum);
                    break;

                case 6:
                    satdayNum++;
                    hashHistogram.put(6, satdayNum);
                    break;

            }
        }
        Log.d("StatisticsFragment", "hashHistogram:" + hashHistogram);
        customHistogramView.setHashHistogram(hashHistogram);
    }


    //根据点击temp获取当前月
    public String getLastMonth(int temp) {
        Calendar cal = Calendar.getInstance();
        cal.add(cal.MONTH, -(temp - 1));
        SimpleDateFormat dft = new SimpleDateFormat("yyyyMM");
        String preMonth = dft.format(cal.getTime());
        Log.d("StatisticsFragment", preMonth);
        return preMonth;
    }

    //由long型时间获取星期几
    public int getDay(Long startTime) {


//前面的lSysTime是秒数，先乘1000得到毫秒数，再转为java.util.Date类型
        Date dt = new Date(startTime);
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        return cal.get(Calendar.DAY_OF_WEEK) - 1;
    }

    //获取指定月份的天数
    public int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }


}
