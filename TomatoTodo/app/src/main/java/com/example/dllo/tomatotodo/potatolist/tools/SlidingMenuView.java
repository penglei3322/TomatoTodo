package com.example.dllo.tomatotodo.potatolist.tools;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dllo.tomatotodo.R;

/**
 * Created by dllo on 16/6/14.
 */
public class SlidingMenuView extends HorizontalScrollView {
    private int mScrollWidth;
    private boolean isOpen;
    private SlidingListener slidingListener;
    private TextView menuTv;

    private ViewPager viewPager;

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    public SlidingMenuView setSlidingListener(SlidingListener slidingListener) {
        this.slidingListener = slidingListener;
        return this;
    }

    public void setVp(ViewPager vp) {
        this.viewPager = vp;
    }

    public SlidingMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setHorizontalScrollBarEnabled(false);
        isOpen = false;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        menuTv = (TextView) findViewById(R.id.deleter);
        //LinearLayout linearLayout= (LinearLayout) findViewById(R.id.slidingMen_linelayout);
        mScrollWidth = menuTv.getWidth();
        scrollTo(mScrollWidth, 0);
    }


    //当用户点击或者滑动的操作
    // 作用来该view 上的时候  就会回调这个方法
    float x;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                //当用户在该view上滑动的时候
                if(ev.getX() < x && !isOpen){
                   return viewPager.onTouchEvent(ev);
                }
                slidingListener.onMove(this);
                break;
            // Log.d("--------","我点了"+slidingListener);
            // return false;
            case MotionEvent.ACTION_UP:
                changeScall();
                return false;
        }
        return super.onTouchEvent(ev);
    }

    private void changeScall() {
        if (getScrollX() < mScrollWidth / 2) {
           // Log.d("SlidingMenuView", "开启");
            smoothScrollTo(0, 0);
            isOpen = true;
            slidingListener.onMenuIsOpen(this);
        } else {
          //  Log.d("SlidingMenuView", "关闭");
            close();

        }
    }

    public void close() {
        smoothScrollTo(mScrollWidth, 0);
        isOpen = false;
    }

    public interface SlidingListener {
        //当我滑动的时候 把自己传出去
        // 是否是同一个slidingMenu
        void onMove(SlidingMenuView slidingMenuView);

        //打开菜单的时候把自己传出去
        void onMenuIsOpen(SlidingMenuView slidingMenuView);
    }
}
