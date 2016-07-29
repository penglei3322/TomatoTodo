package com.example.dllo.tomatotodo.base;

import android.app.Application;
import android.content.Context;

import cn.bmob.v3.Bmob;

/**
 * Created by dllo on 16/7/27.
 */
public class MyApp extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Bmob.initialize(this, "5d1ecaa08f7c8d7a190d0799b1bd5e21");
    }

    public static Context getContext() {
        return context;
    }
}
