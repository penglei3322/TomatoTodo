package com.example.dllo.tomatotodo.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by dllo on 16/7/16.
 * 彭磊-tabLayout的适配器
 */
public class MainAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    private String tab[] = {"土豆列表", "历史", "统计"};

    public void setFragments(List<Fragment> fragments) {
        this.fragments = fragments;
    }

    public MainAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tab[position];
    }
}
