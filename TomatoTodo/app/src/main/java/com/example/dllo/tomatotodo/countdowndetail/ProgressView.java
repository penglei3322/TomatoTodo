package com.example.dllo.tomatotodo.countdowndetail;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

/**
 * Created by zly on 16/7/20.
 */
public class ProgressView extends View  {

    private Paint paintGray;
    private Paint paintWhite;
    private Paint paintProgress;
    private int length;

    private float progress;
    private int color;


    public ProgressView(Context context) {
        super(context);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        length = 400;
        setMeasuredDimension(length, length);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paintGray = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintGray.setColor(Color.LTGRAY);
        paintWhite = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintWhite.setColor(Color.WHITE);
        paintProgress = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintProgress.setColor(color);

        canvas.drawCircle(length / 2, length / 2, length / 2, paintGray);

        if (progress != 0) {
            RectF rectF = new RectF(0, 0, length, length);
            canvas.drawArc(rectF, -90, progress, true, paintProgress);
        }

        canvas.drawCircle(length / 2, length / 2, length / 2 - 20, paintWhite);


    }

}
