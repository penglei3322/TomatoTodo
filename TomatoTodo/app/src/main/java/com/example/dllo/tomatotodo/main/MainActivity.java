package com.example.dllo.tomatotodo.main;

import android.support.design.widget.TabLayout;

import com.example.dllo.tomatotodo.R;
import com.example.dllo.tomatotodo.base.BaseActivity;

public class MainActivity extends BaseActivity {

    private TabLayout tabLayout;
    private MainAdapter adapter;

    @Override
    public int initView() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        tabLayout = (TabLayout) findViewById(R.id.main_tablayout);
        adapter = new MainAdapter(getSupportFragmentManager());
    }
}
