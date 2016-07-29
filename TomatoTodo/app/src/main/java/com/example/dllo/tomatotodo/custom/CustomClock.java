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

import com.example.dllo.tomatotodo.MyApp;
import com.example.dllo.tomatotodo.db.HistoryAllBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by dllo on 16/7/21.
 */
public class CustomClock extends View {

    private int height;
    private int width;
    private Paint paintRect;
    private Paint paintLine;
    private Paint paintCircle;
    private Paint paintArc;
    private Paint paintText;
    private float sin15 = 0.25881904510252F;
    private float sin30 = 0.5F;
    private float sin45 = 0.70710678118655F;
    private float sin60 = 0.86602540378444F;
    private float sin75 = 0.96592582628907F;
    private float cos15 = 0.96592582628907F;
    private ArrayList<HistoryAllBean> beanArrayList;

    public CustomClock(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setBeanArrayList(ArrayList<HistoryAllBean> beanArrayList) {
        this.beanArrayList = beanArrayList;
        Log.d("CustomClock", "beanArrayList.size():" + beanArrayList.size());
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
        height = heightScreen / 3;
        width = widthScreen;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //外点画笔
        paintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintLine.setAntiAlias(true);
        //背景白色方形画笔
        paintRect = new Paint(Paint.ANTI_ALIAS_FLAG);
        //内层圆形画笔
        paintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        //百分比扇形画笔
        paintArc = new Paint(Paint.ANTI_ALIAS_FLAG);

        paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintRect.setColor(Color.WHITE);
        canvas.drawRect(0, 0, height, height, paintRect);
        paintLine.setColor(Color.BLACK);
        paintLine.setStrokeWidth(3);
        canvas.drawLine(width / 4 * 3, height / 2, width / 4 * 3, height / 2 - height / 3 - 10, paintLine);
        //15度
        canvas.drawLine(width / 4 * 3, height / 2, width / 4 * 3 + (height / 3 + 10) * sin15, height / 2 - (height / 3 + 10) * sin75, paintLine);
        //30度
        canvas.drawLine(width / 4 * 3, height / 2, width / 4 * 3 + (height / 3 + 10) * sin30, height / 2 - (height / 3 + 10) * sin60, paintLine);
        //45度
        canvas.drawLine(width / 4 * 3, height / 2, width / 4 * 3 + (height / 3 + 10) * sin45, height / 2 - (height / 3 + 10) * sin45, paintLine);
        //60度
        canvas.drawLine(width / 4 * 3, height / 2, width / 4 * 3 + (height / 3 + 10) * sin60, height / 2 - (height / 3 + 10) * sin30, paintLine);
        //75度
        canvas.drawLine(width / 4 * 3, height / 2, width / 4 * 3 + (height / 3 + 10) * sin75, height / 2 - (height / 3 + 10) * sin15, paintLine);
        //90度
        canvas.drawLine(width / 4 * 3, height / 2, width / 4 * 3 + (height / 3 + 10), height / 2, paintLine);
        //105度
        canvas.drawLine(width / 4 * 3, height / 2, width / 4 * 3 + (height / 3 + 10) * sin15, height / 2 + (height / 3 + 10) * cos15, paintLine);
        //120度
        canvas.drawLine(width / 4 * 3, height / 2, width / 4 * 3 + (height / 3 + 10) * sin30, height / 2 + (height / 3 + 10) * sin60, paintLine);
        //135度
        canvas.drawLine(width / 4 * 3, height / 2, width / 4 * 3 + (height / 3 + 10) * sin45, height / 2 + (height / 3 + 10) * sin45, paintLine);
        //150度
        canvas.drawLine(width / 4 * 3, height / 2, width / 4 * 3 + (height / 3 + 10) * sin60, height / 2 + (height / 3 + 10) * sin30, paintLine);
        //165度
        canvas.drawLine(width / 4 * 3, height / 2, width / 4 * 3 + (height / 3 + 10) * sin75, height / 2 + (height / 3 + 10) * sin15, paintLine);
        //180度
        canvas.drawLine(width / 4 * 3, height / 2, width / 4 * 3, height / 2 + height / 3 + 10, paintLine);
        //195度
        canvas.drawLine(width / 4 * 3, height / 2, width / 4 * 3 - (height / 3 + 10) * sin15, height / 2 + (height / 3 + 10) * sin75, paintLine);
        //210度
        canvas.drawLine(width / 4 * 3, height / 2, width / 4 * 3 - (height / 3 + 10) * sin30, height / 2 + (height / 3 + 10) * sin60, paintLine);
        //225度
        canvas.drawLine(width / 4 * 3, height / 2, width / 4 * 3 - (height / 3 + 10) * sin45, height / 2 + (height / 3 + 10) * sin45, paintLine);
        //240度
        canvas.drawLine(width / 4 * 3, height / 2, width / 4 * 3 - (height / 3 + 10) * sin60, height / 2 + (height / 3 + 10) * sin30, paintLine);
        //255度
        canvas.drawLine(width / 4 * 3, height / 2, width / 4 * 3 - (height / 3 + 10) * sin75, height / 2 + (height / 3 + 10) * sin15, paintLine);
        //270度
        canvas.drawLine(width / 4 * 3, height / 2, width / 4 * 3 - (height / 3 + 10), height / 2, paintLine);
        //285度
        canvas.drawLine(width / 4 * 3, height / 2, width / 4 * 3 - (height / 3 + 10) * sin15, height / 2 - (height / 3 + 10) * sin75, paintLine);
        //300度
        canvas.drawLine(width / 4 * 3, height / 2, width / 4 * 3 - (height / 3 + 10) * sin30, height / 2 - (height / 3 + 10) * sin60, paintLine);
        //315度
        canvas.drawLine(width / 4 * 3, height / 2, width / 4 * 3 - (height / 3 + 10) * sin45, height / 2 - (height / 3 + 10) * sin45, paintLine);
        //330度
        canvas.drawLine(width / 4 * 3, height / 2, width / 4 * 3 - (height / 3 + 10) * sin60, height / 2 - (height / 3 + 10) * sin30, paintLine);
        //345度
        canvas.drawLine(width / 4 * 3, height / 2, width / 4 * 3 - (height / 3 + 10) * sin75, height / 2 - (height / 3 + 10) * sin15, paintLine);
        paintCircle.setColor(Color.WHITE);
        canvas.drawCircle(width / 4 * 3, height / 2, height / 3 + 2, paintCircle);
        paintCircle.setColor(Color.BLACK);
        canvas.drawCircle(width / 4 * 3, height / 2, height / 3 - 4, paintCircle);
        paintCircle.setColor(Color.WHITE);
        canvas.drawCircle(width / 4 * 3, height / 2, height / 3 - 6, paintCircle);

        paintText.setColor(Color.BLACK);
        paintText.setTextSize(40);

        //24时
        canvas.drawText("24", width / 4 * 3 - 20, height / 8, paintText);

        //6时
        canvas.drawText("6", width / 4 * 3 + height / 3 + 20, height / 2 + 10, paintText);

        //12时
        canvas.drawText("12", width / 4 * 3 - 20, height / 15 * 14, paintText);

        //18时
        canvas.drawText("18", width / 4 * 3 - height / 3 - 60, height / 2 + 10, paintText);

        paintText.setTextSize(50);
        canvas.drawText("最佳工作时段", width / 20, height / 5, paintText);

        // 画红色区域
        paintArc.setColor(Color.RED);
        paintArc.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN));
        paintArc.setColor(Color.parseColor("#40ff2a2a"));
        RectF rectF = new RectF((width / 4 * 3 - height / 3) + 6, (height / 2 - height / 3) + 6, (width / 4 * 3 + height / 3) - 6, (height / 2 + height / 3) - 6);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HHmm");

        for (HistoryAllBean historyAllBean : beanArrayList) {
            String startTime = simpleDateFormat.format(new Date(historyAllBean.getStartTime()));
            Log.d("CustomClock111", startTime);

            //将开始的小时里的分钟转换成小时形式
            float hours = Integer.valueOf(startTime.substring(0, 2)) + Integer.valueOf(startTime.substring(2)) / 60f;
            Log.d("CustomClock222", "hours:" + hours);
            //弧形开始角度
            float startAngle = (hours - 6) * 360 / 24;
            //持续的工作时间

            float duringHours = (historyAllBean.getEndTime() - historyAllBean.getStartTime()) / (1000 * 60 * 60f);
            Log.d("CustomClock333", "duringHours:" + duringHours);
            //扇形扫过的角度
            float sweepAngle = duringHours / 24 * 360;
            canvas.drawArc(rectF, startAngle, sweepAngle, true, paintArc);
        }


    }
}
