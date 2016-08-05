package com.example.dllo.tomatotodo.statistics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

    private PullToRefreshScrollView mScrollView;
    private TextView mTvLeft, mTvCenter, mTvRight, tvAll, tvAverage, tvGrowth;
    private HashMap<Integer, String> mMonthMap;
    private String mDate;//当时的日期,用作点击事件判断
    private int mDateKey;//map里日期的key
    private int mTemp = 0;//记录点击的次数
    private ArrayList<HistoryAllBean> mMouthAgoList;
    SimpleDateFormat mFormatMouth = new SimpleDateFormat("MM");
    SimpleDateFormat mFormatYearMouth = new SimpleDateFormat("yyyyMM");
    private CustomLineChartView mLineChartView;
    private HashMap<Integer, Integer> mHashHistogram;
    private CustomHistogramView mCustomHistogramView;
    private CustomClock mCustomClock;
    //分享的广播接收者
    private ShareReceiver receiver;
    //服务广播接收者
    private CompleteReceiver receiverComplete;

    @Override
    public int createView() {
        return R.layout.fragmet_statistics;
    }

    @Override
    public void initView(View view) {
        mTvLeft = (TextView) view.findViewById(R.id.tv_left_fragment_statics);
        mTvCenter = (TextView) view.findViewById(R.id.tv_center_fragment_statics);
        mTvRight = (TextView) view.findViewById(R.id.tv_right_fragment_statics);
        tvAll = (TextView) view.findViewById(R.id.tv_tomato_all);
        tvAverage = (TextView) view.findViewById(R.id.tv_daily_average);
        tvGrowth = (TextView) view.findViewById(R.id.tv_month_growth);
        mScrollView = (PullToRefreshScrollView) view.findViewById(R.id.scrollViewStatics);
        mCustomHistogramView = (CustomHistogramView) view.findViewById(R.id.customHistogramView);
        mLineChartView = (CustomLineChartView) view.findViewById(R.id.customLineChartView);
        mCustomClock = (CustomClock) view.findViewById(R.id.customClock);
        mTvLeft.setOnClickListener(this);
        mTvCenter.setOnClickListener(this);
        mTvRight.setOnClickListener(this);

        mScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                Intent intent = new Intent(MyApp.context,ObjectiveActivity.class);
                startActivity(intent);
                mScrollView.onRefreshComplete();
            }
        });

        //注册分享广播
        receiver = new ShareReceiver();
        IntentFilter filter =new IntentFilter("share");
        context.registerReceiver(receiver,filter);

        receiverComplete = new CompleteReceiver();
        IntentFilter filterCom = new IntentFilter("refreshView");
        context.registerReceiver(receiverComplete,filterCom);

    }

    @Override
    public void initData() {


        mHashHistogram = new HashMap<>();
        //添加HashMap
        mTvCenter.setText("过去三十天");
        mMonthMap = new HashMap<>();
        mMonthMap.put(1, "一月");
        mMonthMap.put(2, "二月");
        mMonthMap.put(3, "三月");
        mMonthMap.put(4, "四月");
        mMonthMap.put(5, "五月");
        mMonthMap.put(6, "六月");
        mMonthMap.put(7, "七月");
        mMonthMap.put(8, "八月");
        mMonthMap.put(9, "九月");
        mMonthMap.put(10, "十月");
        mMonthMap.put(11, "十一月");
        mMonthMap.put(12, "十二月");
        //根据当时时间的月份从hashMap中获取三个TextView的显示内容
        mDate = mFormatMouth.format(new Date());
        //将String类型的date 转化成为int类型
        mDateKey = Integer.valueOf(mDate);
        //根据转换的int类型key值拿到想要的月份
        mTvLeft.setText(mMonthMap.get(mDateKey));
        //设置默认的折线图数据
        setMouthAgoData();


    }
     //默认的最后30天的数据
    private void setMouthAgoData() {
        //30天前 获取30天前的日期(Java)
        Calendar theCa = Calendar.getInstance();
        theCa.setTime(new Date());
        theCa.add(theCa.DATE, -30);
        Date date = theCa.getTime();
        //转型
        long mouthAgo = date.getTime();
        //根据时间遍历出所有符合条件的数据
        mMouthAgoList = new ArrayList();
        for (HistoryAllBean historyAllBean : DBTools.getInstance(context).queryAll(HistoryAllBean.class)) {
            if (historyAllBean.getStartTime() > mouthAgo) {
                mMouthAgoList.add(historyAllBean);
            }

        }
        mCustomClock.setBeanArrayList(mMouthAgoList);
        //添加到自定义折线图
        mLineChartView.setMouthAgoList(mMouthAgoList, "13", 0);
        //最近30天总数
        tvAll.setText(mMouthAgoList.size() + "");
        DecimalFormat fnum = new DecimalFormat("##0.00");
        String dd = fnum.format(mMouthAgoList.size() / 30F);
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
                mDateKey--;
                mTemp++;
                mTvRight.setVisibility(View.VISIBLE);
                //当点击到一月份时候,让TextView可以重新循环整个map
                if (mDateKey == 0) {
                    mDateKey = 12;
                }
                mTvRight.setText(mTvCenter.getText().toString());
                //当点击左侧按钮时,把左侧按钮中的值赋给中间按钮
                mTvCenter.setText(mTvLeft.getText().toString());
                //根据key判断
                int keyLeft = mDateKey % 12;
                if (keyLeft != 0) {
                    mTvLeft.setText(mMonthMap.get(mDateKey % 12));
                } else {
                    mTvLeft.setText(mMonthMap.get(12));
                }
                //每当点击一次左侧按钮,记录一次
                setMouthData(mTemp);
                break;

            case R.id.tv_center_fragment_statics:

                break;

            case R.id.tv_right_fragment_statics:
                mTemp--;
                mDateKey++;
                //首先判断点击的temp次数,如果为0,则拦截点击事件
                if (mTemp == 0) {
                    mTvRight.setVisibility(View.INVISIBLE);
                    mTvCenter.setText("过去三十天");
                    setMouthAgoData();
                    String month = mFormatMouth.format(new Date());
                    int num = Integer.valueOf(month);
                    mTvLeft.setText(mMonthMap.get(num));
                    int keyTemp = mDateKey % 12;
                    if (keyTemp != 0) {
                        mTvLeft.setText(mMonthMap.get(keyTemp));
                    } else {
                        mTvLeft.setText(mMonthMap.get(12));
                    }
                    return;
                }
                if (mTemp == 1) {
                    mTvLeft.setText(mTvCenter.getText().toString());
                    mTvCenter.setText(mTvRight.getText().toString());
                    mTvRight.setText("过去三十天");
                } else if (mTemp == 2) {
                    String month = mFormatMouth.format(new Date());
                    int num = Integer.valueOf(month);
                    mTvRight.setText(mMonthMap.get(num));
                    mTvCenter.setText(mMonthMap.get(num - 1));
                    mTvLeft.setText(mMonthMap.get(num - 2));

                } else {
                    mTvLeft.setText(mTvCenter.getText().toString());
                    mTvCenter.setText(mTvRight.getText().toString());
                    int keyRight = (mDateKey + 2) % 12;
                    if (keyRight != 0) {
                        mTvRight.setText(mMonthMap.get(keyRight));
                    } else {
                        mTvRight.setText(mMonthMap.get(12));
                    }
                }
                setNextMonthDate(mTemp);
                break;


        }
    }

    private void setNextMonthDate(int temp) {
        mMouthAgoList = new ArrayList();
        ArrayList<HistoryAllBean> realNextMonthList = new ArrayList<>();
        for (HistoryAllBean historyAllBean : DBTools.getInstance(context).queryAll(HistoryAllBean.class)) {
            if (DateUtils.getLastMonth(temp).equals(mFormatYearMouth.format(historyAllBean.getStartTime()))) {
                mMouthAgoList.add(historyAllBean);
            }
        }
        //添加饼状图
        mCustomClock.setBeanArrayList(mMouthAgoList);
        mLineChartView.setMouthAgoList(mMouthAgoList, String.valueOf(DateUtils.getLastMonth(temp)),temp);

        String thisMonth = DateUtils.getLastMonth(temp);
        int yearThis = Integer.valueOf(thisMonth.substring(0, 4));
        int monthThis = Integer.valueOf(thisMonth.substring(4));
        //获取当前月的天数
        int thisMonthDays = DateUtils.getDaysByYearMonth(yearThis, monthThis);
        //获取之前一个月的年份月份
        String thisMonthAgo = DateUtils.getLastMonth(temp - 1);
        int yearAgo = Integer.valueOf(thisMonthAgo.substring(0, 4));
        int monthAgo = Integer.valueOf(thisMonthAgo.substring(4));
        //获取之前一个月的天数
        int lastMonthDays = DateUtils.getDaysByYearMonth(yearAgo, monthAgo);


        DecimalFormat fnum = new DecimalFormat("##0.00");

        String dayAverage = fnum.format(mMouthAgoList.size() / (float) thisMonthDays);
        tvAll.setText(mMouthAgoList.size() + "");
        //日平均数
        tvAverage.setText(dayAverage);

        float growth = mMouthAgoList.size() / (float) thisMonthDays - realNextMonthList.size() / (float) lastMonthDays;

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
        mMouthAgoList = new ArrayList();
        ArrayList<HistoryAllBean> realMonthAgoList = new ArrayList<>();

        for (HistoryAllBean historyAllBean : DBTools.getInstance(context).queryAll(HistoryAllBean.class)) {
            if (DateUtils.getLastMonth(temp).equals(mFormatYearMouth.format(historyAllBean.getStartTime()))) {
                mMouthAgoList.add(historyAllBean);
            }
            if (DateUtils.getLastMonth(temp + 1).equals(mFormatYearMouth.format(historyAllBean.getStartTime()))) {
                realMonthAgoList.add(historyAllBean);
            }
        }

        //添加饼状图
        mCustomClock.setBeanArrayList(mMouthAgoList);
        //添加折线图
        mLineChartView.setMouthAgoList(mMouthAgoList, String.valueOf(DateUtils.getLastMonth(temp)),temp);

        String thisMonth = DateUtils.getLastMonth(temp);
        int yearThis = Integer.valueOf(thisMonth.substring(0, 4));
        int monthThis = Integer.valueOf(thisMonth.substring(4));
        //获取当前月的天数
        int thisMonthDays = DateUtils.getDaysByYearMonth(yearThis, monthThis);


        //获取之前一个月的年份月份
        String thisMonthAgo = DateUtils.getLastMonth(temp + 1);
        int yearAgo = Integer.valueOf(thisMonthAgo.substring(0, 4));
        int monthAgo = Integer.valueOf(thisMonthAgo.substring(4));
        //获取之前一个月的天数
        int lastMonthDays = DateUtils.getDaysByYearMonth(yearAgo, monthAgo);

        //统计上方的TextView的数字
        DecimalFormat fnum = new DecimalFormat("##0.00");
        String dayAverage = fnum.format(mMouthAgoList.size() / (float) thisMonthDays);
        tvAll.setText(mMouthAgoList.size() + "");
        //日平均数
        tvAverage.setText(dayAverage);

        float growth = mMouthAgoList.size() / (float) thisMonthDays - realMonthAgoList.size() / (float) lastMonthDays;

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
        mHashHistogram = new HashMap<>();
        int sundayNum = 0;
        int mondayNum = 0;
        int tuesdayNum = 0;
        int wednesdayNum = 0;
        int thursdayNum = 0;
        int fridayNum = 0;
        int satdayNum = 0;
        for (HistoryAllBean historyAllBean : mMouthAgoList) {
            switch (DateUtils.getDay(historyAllBean.getStartTime())) {
                case 0:
                    sundayNum++;
                    mHashHistogram.put(0, sundayNum);
                    break;
                case 1:
                    mondayNum++;
                    mHashHistogram.put(1, mondayNum);
                    break;
                case 2:
                    tuesdayNum++;
                    mHashHistogram.put(2, tuesdayNum);
                    break;

                case 3:
                    wednesdayNum++;
                    mHashHistogram.put(3, wednesdayNum);
                    break;

                case 4:
                    thursdayNum++;
                    mHashHistogram.put(4, thursdayNum);
                    break;

                case 5:
                    fridayNum++;
                    mHashHistogram.put(5, fridayNum);
                    break;

                case 6:
                    satdayNum++;
                    mHashHistogram.put(6, satdayNum);
                    break;

            }
        }
        mCustomHistogramView.setHashHistogram(mHashHistogram);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context.unregisterReceiver(receiver);
        context.unregisterReceiver(receiverComplete);
    }

    class ShareReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("ShareReceiver", "收到了分享广播");
            BitmapUtils.viewSaveToImage(mScrollView);
        }
    }

    class CompleteReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("CompleteReceiver", "222:" + 222);
            initData();
        }
    }


}
