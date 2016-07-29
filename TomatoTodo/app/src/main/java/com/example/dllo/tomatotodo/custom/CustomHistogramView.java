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

import com.example.dllo.tomatotodo.MyApp;

import java.util.HashMap;

/**
 * Created by dllo on 16/7/27.
 * 自定义柱状图
 */
public class CustomHistogramView extends View {

    private int width;
    private int height;
    private Paint paint;
    private Paint paintBackground;
    private Paint paintText;
    private Paint paintRect;
    private String[] day = {"日", "一", "二", "三", "四", "五", "六"};
    private String dayOfWeek;
    private HashMap<Integer, Integer> hashHistogram;
    private int maxNum;
    private int total;
    private float average;

    public void setHashHistogram(HashMap<Integer, Integer> hashHistogram) {
        this.hashHistogram = hashHistogram;
        Log.d("CustomHistogramView", "this.hashHistogram.get(1):" + this.hashHistogram.get(1));
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
        height = heightScreen / 3;
        width = widthScreen;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint = new Paint();
        paintText = new Paint();
        paintRect = new Paint();
        paintRect.setColor(Color.RED);
        paintText.setTextSize(30);
        paint.setColor(Color.parseColor("#F0F0F0"));
        paintBackground = new Paint();
        paintBackground.setColor(Color.WHITE);
        //自定义视图的白色背景
        canvas.drawRect(0, 0, width, height, paintBackground);
        //新建数组用以存放一周中每天的番茄数
        int[] dayEvery = new int[7];
        for (int i = 0; i < 7; i++) {
            if (hashHistogram.containsKey(i)) {
                dayEvery[i] = hashHistogram.get(i);
            } else {
                dayEvery[i] = 0;
            }
        }
        //遍历出数组中的最大值
        for (int i = 0; i < dayEvery.length; i++) {
            if (maxNum < dayEvery[i]) {
                maxNum = dayEvery[i];
                dayOfWeek = day[i];
                total += dayEvery[i];
            }
        }

        average = total / 7f;

        Log.d("CustomHistogramView", "average:" + average);

        //绘制柱状图,并根据各天与最大值的百分比填充颜色
        for (int i = 0; i < 7; i++) {
            canvas.drawRect(width / 9 * 4 + 90 * (i - 1), height / 5, width / 9 * 4 + 90 * (i - 1) + 50, height / 4 * 3, paint);
            canvas.drawText(day[i], width / 9 * 4 + 90 * (i - 1) + 10, height / 4 * 3 + height / 15, paintText);
            if (maxNum == 0){
                return;
            }
            if (dayEvery[i] == maxNum) {
                canvas.drawRect(width / 9 * 4 + 90 * (i - 1), height / 5, width / 9 * 4 + 90 * (i - 1) + 50, height / 4 * 3, paintRect);
            } else {
                Log.d("CustomHistogramView", "maxNum:" + maxNum);
                Log.d("CustomHistogramView", "dayEvery[i]:" + dayEvery[i]);
                int molecular = dayEvery[i];
                Log.d("CustomHistogramView", "molecular:" + molecular);

                canvas.drawRect(width / 9 * 4 + 90 * (i - 1), height / 4 * 3 - (height / 4 * 3 - height / 5) / maxNum * molecular, width / 9 * 4 + 90 * (i - 1) + 50, height / 4 * 3, paintRect);
            }
        }
        paintText.setTextSize(50);
        canvas.drawText("最佳工作日", 25, height / 3, paintText);
        // 画最佳是星期几
        paintText.setTextSize(70);
        if (hashHistogram.isEmpty()) {
            canvas.drawText("暂无数据", 25, height / 2, paintText);
        } else {
            canvas.drawText("星期" + dayOfWeek, 25, height / 2, paintText);
        }

        // 画比平均高出多少
        paintText.setTextSize(40);
        paintText.setAlpha(150);
        if (hashHistogram.isEmpty()) {
            canvas.drawText("比平均高出" + 0 + "%", 25, height / 5 * 3, paintText);
        }else {
            canvas.drawText("比平均高出" + (int) ((maxNum - average) / average * 100) + "%", 25, height / 5 * 3, paintText);
        }


    }
}
