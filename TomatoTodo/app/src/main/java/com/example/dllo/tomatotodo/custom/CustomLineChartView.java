package com.example.dllo.tomatotodo.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.provider.ContactsContract;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.example.dllo.tomatotodo.MyApp;
import com.example.dllo.tomatotodo.db.HistoryAllBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by dllo on 16/7/25.
 * 自定义折线图
 */
public class CustomLineChartView extends View {

    private int height;
    private int width;
    private ArrayList<Integer> yAxis = new ArrayList<>();
    private String dateEvery;
    private Date dateThis;//传入月份转型为Date

    private HashMap<String, Integer> data;
    private int num = 1;
    private int maxNum = 0;
    private int mouth;
    private int lineNum = 0;
    private int everyDayNum;
    private ArrayList<HistoryAllBean> mouthAgoList = new ArrayList();

    //判断年月
    private String thisYearMonth;
    // 网格宽高
    private int gridWidth;
    private int gridHeight;
    //存放着每年的每月有几天
    private int[] mouthDay = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31, 30};
    // 底部高度
    private int bottomHeight;

    // 白色背景画笔
    private Paint paintBg;
    // 灰色网格画笔
    private Paint paintGridLine;
    // 文本数据画笔
    private Paint paintText;

    // 折线圆点背景
    private Paint paintPointBg;
    // 折线圆点红色表面画笔
    private Paint paintPointCircle;

    // 折线的画笔
    private Paint paintLine;


    public CustomLineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void init() {
        data = new HashMap<>();
        paintBg = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintBg.setColor(Color.CYAN);

        paintGridLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintGridLine.setColor(Color.GRAY);
        paintGridLine.setTextAlign(Paint.Align.CENTER);

        paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setColor(Color.BLACK);
        paintText.setTextSize(35);
        paintText.setTextAlign(Paint.Align.CENTER);


        paintPointBg = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintPointBg.setColor(Color.WHITE);

        paintPointCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintPointCircle.setColor(Color.RED);

        paintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintLine.setColor(Color.RED);
        paintLine.setStrokeWidth(5);
        num = 1;
        for (int i = 0; i < mouthAgoList.size() - 1; i++) {
            SimpleDateFormat formatStart = new SimpleDateFormat("MM-dd");
            String startData = formatStart.format(mouthAgoList.get(i).getStartTime());
            if (startData.equals(formatStart.format(mouthAgoList.get(i + 1).getStartTime()))) {
                num++;
                data.put(startData, num);
                if (num > maxNum) {
                    maxNum = num;
                    Log.d("CustomLineChartView", "maxNum:" + maxNum);
                }
            } else {
                num = 1;
            }


        }

        if (maxNum < 4) {
            maxNum = 4;
        }

        WindowManager manager = (WindowManager) MyApp.context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int widthScreen = outMetrics.widthPixels;
        int heightScreen = outMetrics.heightPixels;
        gridHeight = (height - 120) / maxNum;
        gridWidth = widthScreen / 7;
        bottomHeight = 20;

    }

    public void setMouthAgoList(ArrayList mouthAgoList, String mouth) {

        thisYearMonth = mouth;
        this.mouthAgoList = mouthAgoList;
        if (mouth.equals("13")) {
            this.mouth = 13;
            lineNum = 30;
        } else {
            this.mouth = Integer.valueOf(mouth.substring(4));
            if (this.mouth == 0) {
                this.mouth = 12;
            }

            getDaysByYearMonth(Integer.valueOf(mouth.substring(0,4)),Integer.valueOf(mouth.substring(5)));


        }

        invalidate();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        init();
        super.onDraw(canvas);
        //当每次重新绘制的时候清空存放
        yAxis.clear();
        canvas.drawColor(Color.WHITE);
        // 画横线
        for (int i = 0; i < maxNum + 1; i++) {
            canvas.drawLine(0, height - gridHeight * i - 80, width, height - gridHeight * i - 80, paintGridLine);
        }


        Date date = new Date();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
        try {
             dateThis = simpleDateFormat.parse(thisYearMonth);
        } catch (ParseException e) {
            e.printStackTrace();
        }



        //判断传进来的数据是否是最近30天

        if (mouth ==13){
            lineNum = mouthDay[mouth - 1];
        } else {
            //传入方法返回月份所有日期绘制x坐标
            dayReport(canvas,dateThis);
        }

        //画竖线
        for (int i = 0; i < lineNum; i++) {
            canvas.drawLine(gridWidth / 2 + gridWidth * i, 0, gridWidth / 2 + gridWidth * i, height - 80, paintGridLine);

            SimpleDateFormat format = new SimpleDateFormat("MM/dd");
            if (mouth == 13) {
                dateEvery = format.format(getNextDayThirtyDay(date, lineNum - i));
            } else {
                Date dateLast =  getLastMonthDay(date, 1);
                dateEvery = format.format(getNextDay(dateLast, -(lineNum + i-1)));
            }

            if (mouth == 13){
                canvas.drawText(dateEvery, gridWidth / 2 + gridWidth * i, height - 30, paintText);
            }

            //存放每个坐标点的值
            for (HistoryAllBean historyAllBean : mouthAgoList) {
                if (format.format(historyAllBean.getStartTime()).equals(dateEvery)) {
                    everyDayNum++;

                }
            }
            yAxis.add(everyDayNum);
            everyDayNum = 0;

        }
        //折线

        for (int i = 0; i < lineNum - 1; i++) {
            canvas.drawLine(gridWidth / 2 + gridWidth * i, height - 80 - gridHeight * yAxis.get(i),
                    gridWidth / 2 + gridWidth * (i + 1), height - 80 - gridHeight * yAxis.get(i + 1), paintLine);

        }
        //圆点
        for (int i = 0; i < lineNum; i++) {
            canvas.drawCircle(gridWidth/ 2 + gridWidth * i, height - 80 - gridHeight * yAxis.get(i), 20, paintPointCircle);
            canvas.drawCircle(gridWidth / 2 + gridWidth * i, height - 80 - gridHeight * yAxis.get(i), 15, paintPointBg);

        }

    }

    public static Date getLastMonthDay(Date date,int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -i);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        date = calendar.getTime();
        return date;


    }


    public Date getNextDayThirtyDay(Date date, int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -i + 1);
        date = calendar.getTime();
        return date;
    }

    public Date getNextDay(Date date, int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -i);
        date = calendar.getTime();
        return date;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        WindowManager manager = (WindowManager) MyApp.context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int widthScreen = outMetrics.widthPixels;
        int heightScreen = outMetrics.heightPixels;
        height = heightScreen / 3;
        gridWidth = widthScreen / 7;
        width = gridWidth * 31;
        bottomHeight = 20;
        setMeasuredDimension(width, height);
    }

    //获取指定月份的天数
    public  int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        lineNum = maxDate;
        Log.d("CustomLineChartView", "lineNum:" + lineNum);
        return maxDate;
    }

    //根据month获取当前日期月份的总天数绘制出x坐标
    public void dayReport(Canvas canvas,Date month) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(month);//month 为指定月份任意日期
        int year = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH);
        int dayNumOfMonth = getDaysByYearMonth(year, m);
        cal.set(Calendar.DAY_OF_MONTH, 1);// 从一号开始

        for (int i = 0; i < dayNumOfMonth; i++, cal.add(Calendar.DATE, 1)) {
            Date d = cal.getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
            String df = simpleDateFormat.format(d);
            canvas.drawText(df,gridWidth / 2 + gridWidth * i, height - 30, paintText);
        }
    }


}
