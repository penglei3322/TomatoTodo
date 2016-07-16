package com.example.dllo.tomatotodo.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by dllo on 16/7/16.
 * Activity的基类,不要改动!!
 *
 */
public abstract class BaseActivity extends AppCompatActivity{

    public abstract int initView();

    public abstract void initData();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initView());
        initData();
    }

}
