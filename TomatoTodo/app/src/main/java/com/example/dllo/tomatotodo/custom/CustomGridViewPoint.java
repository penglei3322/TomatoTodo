package com.example.dllo.tomatotodo.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.example.dllo.tomatotodo.MyApp;

/**
 * Created by dllo on 16/7/28.
 */
public class CustomGridViewPoint extends View{

    private Paint mPaintInside;
    private Paint mPaintArc;
    private Paint mPaintOut;
    private Paint mPaintNum;
    private String num = null;

    //屏幕宽度
    private int mWidth;
    private int mHeight;
    private int progress;
    private boolean isTouch = false;

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    public void setNum(String num) {
        this.num = num;
        invalidate();
    }

    public CustomGridViewPoint(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //计算宽高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        WindowManager manager = (WindowManager) MyApp.context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int widthScreen = outMetrics.widthPixels;
        int heightScreen = outMetrics.heightPixels;
        mWidth = widthScreen / 12;

        mHeight = mWidth;
        setMeasuredDimension(mWidth, mWidth);
    }

    public void setTouch(boolean touch) {
        isTouch = touch;
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaintInside = new Paint();
        mPaintArc = new Paint();
        mPaintOut = new Paint();
        mPaintNum = new Paint();
        mPaintInside.setColor(Color.GRAY);
        canvas.drawCircle(mWidth / 2, mWidth / 2, mWidth / 2, mPaintInside);
        RectF rectF = new RectF(0, 0, mWidth, mWidth);
        mPaintArc.setColor(Color.GREEN);
        canvas.drawArc(rectF, -90, progress, true, mPaintArc);
        if (isTouch) {
            mPaintOut.setColor(Color.parseColor("#F0F0F0"));
            canvas.drawCircle(mWidth / 2, mWidth / 2, mWidth / 2 - 10, mPaintOut);
            mPaintNum.setColor(Color.BLACK);
            mPaintNum.setTextSize(36);
            mPaintNum.setTextAlign(Paint.Align.CENTER);
            if (num == null) {
                canvas.drawText("0", mWidth / 2  , mWidth / 2 + mWidth / 8, mPaintNum);
            } else {
                canvas.drawText(num, mWidth / 2, mWidth / 2 + mWidth /8, mPaintNum);
                num = null;
            }


        }


    }


}
