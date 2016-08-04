package com.example.dllo.tomatotodo.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.example.dllo.tomatotodo.base.MyApp;
import com.example.dllo.tomatotodo.db.HistoryAllBean;
import com.example.dllo.tomatotodo.statistics.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by dllo on 16/7/25.
 * 自定义折线图
 */
public class CustomLineChartView extends View {
    private int mHeight;
    private int mWidth;
    private ArrayList<Integer> mYAxis = new ArrayList<>();
    private String mDateEvery;
    private Date mDateThis;//传入月份转型为Date
    private HashMap<String, Integer> mHashData;
    private int mNum = 1;
    private int mMaxNum = 0;
    private int mMouth;
    private int mLineNum = 0;
    private int mEveryDayNum;
    private ArrayList<HistoryAllBean> mouthAgoList = new ArrayList();
    private int mTemp;
    //判断年月
    private String mThisYearMonth;
    // 网格宽高
    private int mGridWidth;
    private int mGridHeight;
    //存放着每年的每月有几天
    private int[] mMouthDayNum = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31, 30};
    // 白色背景画笔
    private Paint mPaintBg;
    // 灰色网格画笔
    private Paint mPaintGridLine;
    // 文本数据画笔
    private Paint mPaintText;
    // 折线圆点背景
    private Paint mPaintPointBg;
    // 折线圆点红色表面画笔
    private Paint mPaintPointCircle;
    // 折线的画笔
    private Paint mPaintLine;


    public CustomLineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void init() {
        mHashData = new HashMap<>();
        mPaintBg = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBg.setColor(Color.CYAN);
        mPaintGridLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintGridLine.setColor(Color.GRAY);
        mPaintGridLine.setTextAlign(Paint.Align.CENTER);
        mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintText.setColor(Color.BLACK);
        mPaintText.setTextSize(35);
        mPaintText.setTextAlign(Paint.Align.CENTER);
        mPaintPointBg = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintPointBg.setColor(Color.WHITE);
        mPaintPointCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintPointCircle.setColor(Color.RED);
        mPaintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintLine.setColor(Color.RED);
        mPaintLine.setStrokeWidth(5);

        mNum = 1;
        for (int i = 0; i < mouthAgoList.size() - 1; i++) {
            SimpleDateFormat formatStart = new SimpleDateFormat("MM-dd");
            String startData = formatStart.format(mouthAgoList.get(i).getStartTime());
            if (startData.equals(formatStart.format(mouthAgoList.get(i + 1).getStartTime()))) {
                mNum++;
                mHashData.put(startData, mNum);
                if (mNum > mMaxNum) {
                    mMaxNum = mNum;
                }
            } else {
                mNum = 1;
            }
        }

        if (mMaxNum < 4) {
            mMaxNum = 4;
        }

        WindowManager manager = (WindowManager) MyApp.context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int widthScreen = outMetrics.widthPixels;
        int heightScreen = outMetrics.heightPixels;
        mGridHeight = (mHeight - 120) / mMaxNum;
        mGridWidth = widthScreen / 7;

    }

    public void setMouthAgoList(ArrayList mouthAgoList, String mouth,int temp) {
        //初始化折线图最大值,保证Y轴横线数量
        mMaxNum= 0;
        mThisYearMonth = mouth;
        this.mTemp = temp;
        this.mouthAgoList = mouthAgoList;
        if (mouth.equals("13")) {
            this.mMouth = 13;
            mLineNum = 30;
        } else {
            this.mMouth = Integer.valueOf(mouth.substring(4));
            if (this.mMouth == 0) {
                this.mMouth = 12;
            }
            //截取年份和月份获取月份天数
            mLineNum = getDaysByYearMonth(Integer.valueOf(mouth.substring(0, 4)), Integer.valueOf(mouth.substring(4)));
        }
        invalidate();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        init();
        super.onDraw(canvas);
        //当每次重新绘制的时候清空存放
        mYAxis.clear();
        canvas.drawColor(Color.WHITE);
        // 画横线
        for (int i = 0; i < mMaxNum + 1; i++) {
            canvas.drawLine(0, mHeight - mGridHeight * i - 80, mWidth, mHeight - mGridHeight * i - 80, mPaintGridLine);
        }

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
        try {
            mDateThis = simpleDateFormat.parse(mThisYearMonth);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //判断传进来的数据是否是最近30天
        if (mMouth == 13) {
            mLineNum = mMouthDayNum[mMouth - 1];
        } else {
            //传入方法返回月份所有日期绘制x坐标
            dayReport(canvas, mDateThis);
        }

        //画竖线
        for (int i = 0; i < mLineNum; i++) {
            canvas.drawLine(mGridWidth / 2 + mGridWidth * i, 0, mGridWidth / 2 + mGridWidth * i, mHeight - 80, mPaintGridLine);

            SimpleDateFormat format = new SimpleDateFormat("MM/dd");
            if (mMouth == 13) {
                mDateEvery = format.format(DateUtils.getNextDayThirtyDay(date, mLineNum - i));
            } else {
                //根据点击次数返回对应月份的所有天数
                Date dateLast = DateUtils.getLastMonthDay(date, mTemp);
                //转型为月份与日期
                mDateEvery = format.format(DateUtils.getNextDay(dateLast, -(mLineNum + i)));
            }

            if (mMouth == 13) {
                canvas.drawText(mDateEvery, mGridWidth / 2 + mGridWidth * i, mHeight - 30, mPaintText);
            }
            //存放每个坐标点的值
            for (HistoryAllBean historyAllBean : mouthAgoList) {
                if (format.format(historyAllBean.getStartTime()).equals(mDateEvery)) {
                    mEveryDayNum++;
                }
            }

            mYAxis.add(mEveryDayNum);
            mEveryDayNum = 0;

        }
        //折线
        for (int i = 0; i < mLineNum - 1; i++) {
            canvas.drawLine(mGridWidth / 2 + mGridWidth * i, mHeight - 80 - mGridHeight * mYAxis.get(i),
                    mGridWidth / 2 + mGridWidth * (i + 1), mHeight - 80 - mGridHeight * mYAxis.get(i + 1), mPaintLine);

        }
        //圆点
        for (int i = 0; i < mLineNum; i++) {
            canvas.drawCircle(mGridWidth / 2 + mGridWidth * i, mHeight - 80 - mGridHeight * mYAxis.get(i), 20, mPaintPointCircle);
            canvas.drawCircle(mGridWidth / 2 + mGridWidth * i, mHeight - 80 - mGridHeight * mYAxis.get(i), 15, mPaintPointBg);
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        WindowManager manager = (WindowManager) MyApp.context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int widthScreen = outMetrics.widthPixels;
        int heightScreen = outMetrics.heightPixels;
        mHeight = heightScreen / 3;
        mGridWidth = widthScreen / 7;
        mWidth = mGridWidth * 31;
        setMeasuredDimension(mWidth, mHeight);
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

    //根据month获取当前日期月份的总天数绘制出x坐标
    public void dayReport(Canvas canvas, Date month) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(month);//month 为指定月份任意日期
        int year = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH) + 1;
        int dayNumOfMonth = getDaysByYearMonth(year, m);
        cal.set(Calendar.DAY_OF_MONTH, 1);// 从一号开始

        for (int i = 0; i < dayNumOfMonth; i++, cal.add(Calendar.DATE, 1)) {
            Date d = cal.getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
            String df = simpleDateFormat.format(d);
            canvas.drawText(df, mGridWidth / 2 + mGridWidth * i, mHeight - 30, mPaintText);
        }
    }


}
