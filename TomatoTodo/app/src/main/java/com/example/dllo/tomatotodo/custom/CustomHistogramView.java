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

import java.util.HashMap;

/**
 * Created by dllo on 16/7/27.
 * 自定义柱状图
 */
public class CustomHistogramView extends View {

    private int mWidth;
    private int mHeight;
    private Paint mPaint;
    private Paint mPaintBackground;
    private Paint mPaintText;
    private Paint mPaintRect;
    private String[] mDay = {"日", "一", "二", "三", "四", "五", "六"};
    private String mDayOfWeek;
    private HashMap<Integer, Integer> mHashHistogram;
    private int mMaxNum;
    private int mTotal;
    private float mAverage;

    public void setHashHistogram(HashMap<Integer, Integer> hashHistogram) {
        this.mHashHistogram = hashHistogram;
        //每次点击更换月份时,初始化maxNum避免因为之前月份的maxNum遗留而影响自定义View的绘制
        invalidate();
    }

    public CustomHistogramView(Context context, AttributeSet attrs) {
        super(context, attrs);
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
        mPaint = new Paint();
        mPaintText = new Paint();
        mPaintRect = new Paint();
        mPaintRect.setColor(Color.RED);
        mPaintText.setTextSize(30);
        mPaint.setColor(Color.parseColor("#F0F0F0"));
        mPaintBackground = new Paint();
        mPaintBackground.setColor(Color.WHITE);
        //自定义视图的白色背景
        canvas.drawRect(0, 0, mWidth, mHeight, mPaintBackground);
        //新建数组用以存放一周中每天的番茄数
        int[] dayEvery = new int[7];
        for (int i = 0; i < 7; i++) {
            if (mHashHistogram.containsKey(i)) {
                dayEvery[i] = mHashHistogram.get(i);
            } else {
                dayEvery[i] = 0;
            }
        }
        //遍历出数组中的最大值
        for (int i = 0; i < dayEvery.length; i++) {
            if (mMaxNum < dayEvery[i]) {
                mMaxNum = dayEvery[i];
                mDayOfWeek = mDay[i];
                mTotal += dayEvery[i];
            }
        }
        mAverage = mTotal / 7f;


        //绘制柱状图,并根据各天与最大值的百分比填充颜色
        for (int i = 0; i < 7; i++) {
            canvas.drawRect(mWidth / 9 * 4 + 90 * (i - 1), mHeight / 5, mWidth / 9 * 4 + 90 * (i - 1) + 50, mHeight / 4 * 3, mPaint);
            canvas.drawText(mDay[i], mWidth / 9 * 4 + 90 * (i - 1) + 10, mHeight / 4 * 3 + mHeight / 15, mPaintText);

            if (dayEvery[i] == mMaxNum) {
                if (mMaxNum != 0) {
                    canvas.drawRect(mWidth / 9 * 4 + 90 * (i - 1), mHeight / 5, mWidth / 9 * 4 + 90 * (i - 1) + 50, mHeight / 4 * 3, mPaintRect);
                }
            } else {
                int molecular = dayEvery[i];

                canvas.drawRect(mWidth / 9 * 4 + 90 * (i - 1), mHeight / 4 * 3 - (mHeight / 4 * 3 - mHeight / 5) / mMaxNum * molecular, mWidth / 9 * 4 + 90 * (i - 1) + 50, mHeight / 4 * 3, mPaintRect);


            }
        }
        mPaintText.setTextSize(50);
        canvas.drawText("最佳工作日", 25, mHeight / 3, mPaintText);
        // 画最佳是星期几
        mPaintText.setTextSize(70);
        if (mHashHistogram.isEmpty()) {
            canvas.drawText("暂无数据", 25, mHeight / 2, mPaintText);
        } else {
            canvas.drawText("星期" + mDayOfWeek, 25, mHeight / 2, mPaintText);
        }

        // 画比平均高出多少
        mPaintText.setTextSize(40);
        mPaintText.setAlpha(150);
        if (mHashHistogram.isEmpty()) {
            canvas.drawText("比平均高出" + 0 + "%", 25, mHeight / 5 * 3, mPaintText);
        } else {
            Log.d("CustomHistogramView4444", "average:" + mAverage);
            canvas.drawText("比平均高出" + (int) ((mMaxNum - mAverage) / mAverage * 100) + "%", 25, mHeight / 5 * 3, mPaintText);
        }
        mMaxNum = 0;
        mAverage = 0;
        mTotal = 0;

    }
}
