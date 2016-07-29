package com.example.dllo.tomatotodo.potatolist.tools;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by dllo on 16/6/14.
 */
public class Utils {

    //获取屏幕的宽度

    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        // 创建一个 DisplayMetrics
        //屏幕的参数都是通过该类来获得
        DisplayMetrics displayMetrics = new DisplayMetrics();
        // 调用windowManager的getDefaultDisplay方法
        // 对displayMetrics 参数赋值
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        //输出的是屏幕高度
        return displayMetrics.widthPixels;
    }


}
