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
public class ReturnView extends View {

    private int lenth;


    public ReturnView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        lenth = 60;
        setMeasuredDimension(lenth, lenth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStrokeWidth(6);
        paint.setColor(Color.RED);
        canvas.drawLine(0, lenth / 2, lenth/2, 0, paint);
        canvas.drawLine(0, lenth / 2, lenth/2, lenth, paint);

    }
}
