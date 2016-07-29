package com.example.dllo.tomatotodo;

import android.app.Application;
import android.content.Context;

/**
 * Created by dllo on 16/7/25.
 */
public class MyApp extends Application{

    public static Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

    }

}
