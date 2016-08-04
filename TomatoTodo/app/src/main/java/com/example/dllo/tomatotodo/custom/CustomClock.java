package com.example.dllo.tomatotodo.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.example.dllo.tomatotodo.base.MyApp;
import com.example.dllo.tomatotodo.db.HistoryAllBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by dllo on 16/7/21.
 */
public class CustomClock extends View {

    private int mHeight;
    private int mWidth;
    private Paint mPaintRect;
    private Paint mPaintLine;
    private Paint mPaintCircle;
    private Paint mPaintArc;
    private Paint mPaintText;
    private float sin15 = 0.25881904510252F;
    private float sin30 = 0.5F;
    private float sin45 = 0.70710678118655F;
    private float sin60 = 0.86602540378444F;
    private float sin75 = 0.96592582628907F;
    private float cos15 = 0.96592582628907F;
    private ArrayList<HistoryAllBean> mBeanArrayList;


    public CustomClock(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setBeanArrayList(ArrayList<HistoryAllBean> beanArrayList) {
        this.mBeanArrayList = beanArrayList;
        invalidate();
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
        mWidth = widthScreen;
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //外点画笔
        mPaintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintLine.setAntiAlias(true);
        //背景白色方形画笔
        mPaintRect = new Paint(Paint.ANTI_ALIAS_FLAG);
        //内层圆形画笔
        mPaintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        //百分比扇形画笔
        mPaintArc = new Paint(Paint.ANTI_ALIAS_FLAG);

        mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintRect.setColor(Color.WHITE);
        canvas.drawRect(0, 0, mHeight, mHeight, mPaintRect);
        mPaintLine.setColor(Color.BLACK);
        mPaintLine.setStrokeWidth(3);
        canvas.drawLine(mWidth / 4 * 3, mHeight / 2, mWidth / 4 * 3, mHeight / 2 - mHeight / 3 - 10, mPaintLine);
        //15度
        canvas.drawLine(mWidth / 4 * 3, mHeight / 2, mWidth / 4 * 3 + (mHeight / 3 + 10) * sin15, mHeight / 2 - (mHeight / 3 + 10) * sin75, mPaintLine);
        //30度
        canvas.drawLine(mWidth / 4 * 3, mHeight / 2, mWidth / 4 * 3 + (mHeight / 3 + 10) * sin30, mHeight / 2 - (mHeight / 3 + 10) * sin60, mPaintLine);
        //45度
        canvas.drawLine(mWidth / 4 * 3, mHeight / 2, mWidth / 4 * 3 + (mHeight / 3 + 10) * sin45, mHeight / 2 - (mHeight / 3 + 10) * sin45, mPaintLine);
        //60度
        canvas.drawLine(mWidth / 4 * 3, mHeight / 2, mWidth / 4 * 3 + (mHeight / 3 + 10) * sin60, mHeight / 2 - (mHeight / 3 + 10) * sin30, mPaintLine);
        //75度
        canvas.drawLine(mWidth / 4 * 3, mHeight / 2, mWidth / 4 * 3 + (mHeight / 3 + 10) * sin75, mHeight / 2 - (mHeight / 3 + 10) * sin15, mPaintLine);
        //90度
        canvas.drawLine(mWidth / 4 * 3, mHeight / 2, mWidth / 4 * 3 + (mHeight / 3 + 10), mHeight / 2, mPaintLine);
        //105度
        canvas.drawLine(mWidth / 4 * 3, mHeight / 2, mWidth / 4 * 3 + (mHeight / 3 + 10) * sin15, mHeight / 2 + (mHeight / 3 + 10) * cos15, mPaintLine);
        //120度
        canvas.drawLine(mWidth / 4 * 3, mHeight / 2, mWidth / 4 * 3 + (mHeight / 3 + 10) * sin30, mHeight / 2 + (mHeight / 3 + 10) * sin60, mPaintLine);
        //135度
        canvas.drawLine(mWidth / 4 * 3, mHeight / 2, mWidth / 4 * 3 + (mHeight / 3 + 10) * sin45, mHeight / 2 + (mHeight / 3 + 10) * sin45, mPaintLine);
        //150度
        canvas.drawLine(mWidth / 4 * 3, mHeight / 2, mWidth / 4 * 3 + (mHeight / 3 + 10) * sin60, mHeight / 2 + (mHeight / 3 + 10) * sin30, mPaintLine);
        //165度
        canvas.drawLine(mWidth / 4 * 3, mHeight / 2, mWidth / 4 * 3 + (mHeight / 3 + 10) * sin75, mHeight / 2 + (mHeight / 3 + 10) * sin15, mPaintLine);
        //180度
        canvas.drawLine(mWidth / 4 * 3, mHeight / 2, mWidth / 4 * 3, mHeight / 2 + mHeight / 3 + 10, mPaintLine);
        //195度
        canvas.drawLine(mWidth / 4 * 3, mHeight / 2, mWidth / 4 * 3 - (mHeight / 3 + 10) * sin15, mHeight / 2 + (mHeight / 3 + 10) * sin75, mPaintLine);
        //210度
        canvas.drawLine(mWidth / 4 * 3, mHeight / 2, mWidth / 4 * 3 - (mHeight / 3 + 10) * sin30, mHeight / 2 + (mHeight / 3 + 10) * sin60, mPaintLine);
        //225度
        canvas.drawLine(mWidth / 4 * 3, mHeight / 2, mWidth / 4 * 3 - (mHeight / 3 + 10) * sin45, mHeight / 2 + (mHeight / 3 + 10) * sin45, mPaintLine);
        //240度
        canvas.drawLine(mWidth / 4 * 3, mHeight / 2, mWidth / 4 * 3 - (mHeight / 3 + 10) * sin60, mHeight / 2 + (mHeight / 3 + 10) * sin30, mPaintLine);
        //255度
        canvas.drawLine(mWidth / 4 * 3, mHeight / 2, mWidth / 4 * 3 - (mHeight / 3 + 10) * sin75, mHeight / 2 + (mHeight / 3 + 10) * sin15, mPaintLine);
        //270度
        canvas.drawLine(mWidth / 4 * 3, mHeight / 2, mWidth / 4 * 3 - (mHeight / 3 + 10), mHeight / 2, mPaintLine);
        //285度
        canvas.drawLine(mWidth / 4 * 3, mHeight / 2, mWidth / 4 * 3 - (mHeight / 3 + 10) * sin15, mHeight / 2 - (mHeight / 3 + 10) * sin75, mPaintLine);
        //300度
        canvas.drawLine(mWidth / 4 * 3, mHeight / 2, mWidth / 4 * 3 - (mHeight / 3 + 10) * sin30, mHeight / 2 - (mHeight / 3 + 10) * sin60, mPaintLine);
        //315度
        canvas.drawLine(mWidth / 4 * 3, mHeight / 2, mWidth / 4 * 3 - (mHeight / 3 + 10) * sin45, mHeight / 2 - (mHeight / 3 + 10) * sin45, mPaintLine);
        //330度
        canvas.drawLine(mWidth / 4 * 3, mHeight / 2, mWidth / 4 * 3 - (mHeight / 3 + 10) * sin60, mHeight / 2 - (mHeight / 3 + 10) * sin30, mPaintLine);
        //345度
        canvas.drawLine(mWidth / 4 * 3, mHeight / 2, mWidth / 4 * 3 - (mHeight / 3 + 10) * sin75, mHeight / 2 - (mHeight / 3 + 10) * sin15, mPaintLine);
        mPaintCircle.setColor(Color.WHITE);
        canvas.drawCircle(mWidth / 4 * 3, mHeight / 2, mHeight / 3 + 2, mPaintCircle);
        mPaintCircle.setColor(Color.BLACK);
        canvas.drawCircle(mWidth / 4 * 3, mHeight / 2, mHeight / 3 - 4, mPaintCircle);
        mPaintCircle.setColor(Color.WHITE);
        canvas.drawCircle(mWidth / 4 * 3, mHeight / 2, mHeight / 3 - 6, mPaintCircle);

        mPaintText.setColor(Color.BLACK);
        mPaintText.setTextSize(40);

        //24时
        canvas.drawText("24", mWidth / 4 * 3 - 20, mHeight / 8, mPaintText);

        //6时
        canvas.drawText("6", mWidth / 4 * 3 + mHeight / 3 + 20, mHeight / 2 + 10, mPaintText);

        //12时
        canvas.drawText("12", mWidth / 4 * 3 - 20, mHeight / 15 * 14, mPaintText);

        //18时
        canvas.drawText("18", mWidth / 4 * 3 - mHeight / 3 - 60, mHeight / 2 + 10, mPaintText);

        mPaintText.setTextSize(50);
        canvas.drawText("最佳工作时段", mWidth / 20, mHeight / 5, mPaintText);

        // 画红色区域
        mPaintArc.setColor(Color.RED);
        mPaintArc.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN));
        mPaintArc.setColor(Color.parseColor("#40ff2a2a"));
        RectF rectF = new RectF((mWidth / 4 * 3 - mHeight / 3) + 6, (mHeight / 2 - mHeight / 3) + 6, (mWidth / 4 * 3 + mHeight / 3) - 6, (mHeight / 2 + mHeight / 3) - 6);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HHmm");

        for (HistoryAllBean historyAllBean : mBeanArrayList) {
            String startTime = simpleDateFormat.format(new Date(historyAllBean.getStartTime()));
            //将开始的小时里的分钟转换成小时形式
            float hours = Integer.valueOf(startTime.substring(0, 2)) + Integer.valueOf(startTime.substring(2)) / 60f;
            //弧形开始角度
            float startAngle = (hours - 6) * 360 / 24;
            //持续的工作时间
            float duringHours = (historyAllBean.getEndTime() - historyAllBean.getStartTime()) / (1000 * 60 * 60f);
            //扇形扫过的角度
            float sweepAngle = duringHours / 24 * 360;
            canvas.drawArc(rectF, startAngle, sweepAngle, true, mPaintArc);
        }


    }
}
