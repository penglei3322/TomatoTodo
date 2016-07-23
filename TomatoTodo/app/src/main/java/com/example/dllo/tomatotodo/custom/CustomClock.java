package com.example.dllo.tomatotodo.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by dllo on 16/7/21.
 */
public class CustomClock extends View {

    private int height = 100;
    private Paint paintCircle;
    private Paint paintArc;

    public CustomClock(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(height, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paintCircle = new Paint();
        paintArc = new Paint();
        paintCircle.setColor(Color.parseColor("F0F0F0"));
        canvas.drawCircle(height / 2, height / 2, (height * 1) / 3, paintCircle);


    }
}
