package com.example.dllo.tomatotodo.main;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.example.dllo.tomatotodo.R;
import com.example.dllo.tomatotodo.base.BaseActivity;
import com.example.dllo.tomatotodo.potatolist.PotatoListFragment;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private TabLayout tabLayout;
    private MainAdapter adapter;
    private ArrayList<Fragment>fragments;
    private ViewPager viewPager;


    @Override
    public int initView() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        tabLayout = (TabLayout) findViewById(R.id.main_tablayout);
        adapter = new MainAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.main_viewpager);
        fragments = new ArrayList<>();
        fragments.add(new PotatoListFragment());
        adapter.setFragments(fragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


    }
}
