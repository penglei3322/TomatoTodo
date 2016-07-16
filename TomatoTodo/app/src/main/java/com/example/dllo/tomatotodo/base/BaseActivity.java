package com.example.dllo.tomatotodo.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by dllo on 16/7/16.
 */
public abstract class BaseActivity extends Activity {

    public abstract int initView();
    
    public abstract void initData();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initView());
        initData();
    }


}
