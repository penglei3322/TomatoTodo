package com.example.dllo.tomatotodo.custom;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by dllo on 16/7/21.
 */
public class CustomTextView extends TextView{
    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                setBackgroundColor(Color.parseColor("#cecece"));
                break;

            case MotionEvent.ACTION_MOVE:
                setBackgroundColor(Color.GRAY);
                break;

            case MotionEvent.ACTION_UP:
                setBackgroundColor(Color.parseColor("#404040"));
                break;


        }

        return true;
    }




}
