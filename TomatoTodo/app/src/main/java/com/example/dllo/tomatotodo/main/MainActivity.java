package com.example.dllo.tomatotodo.main;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.dllo.tomatotodo.R;
import com.example.dllo.tomatotodo.base.BaseActivity;
import com.example.dllo.tomatotodo.potatolist.PotatoListFragment;
import com.example.dllo.tomatotodo.service.CountDownEvent;
import com.example.dllo.tomatotodo.service.TomatoService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private TabLayout tabLayout;
    private MainAdapter adapter;
    private ArrayList<Fragment>fragments;
    private ViewPager viewPager;
    private TextView titleTimer;
    private CheckBox startCb;
    private ServiceConnection serviceConnection;
    private TomatoService.MyBinder myBinder;


    @Override
    public int initView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        tabLayout = (TabLayout) findViewById(R.id.main_tablayout);
        adapter = new MainAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.main_viewpager);
        titleTimer = (TextView) findViewById(R.id.title_timer);
        startCb = (CheckBox) findViewById(R.id.title_action_checkbox);
        fragments = new ArrayList<>();
        fragments.add(new PotatoListFragment());
        adapter.setFragments(fragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        // 绑定服务
        Intent serviceIntent = new Intent(this, TomatoService.class);
//        startService(serviceIntent);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                myBinder = (TomatoService.MyBinder) service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE);

        startCb.setOnCheckedChangeListener(this);
    }

    @Subscribe
    public void setTitleTimer(CountDownEvent countDownEvent){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        String time = simpleDateFormat.format(new Date(countDownEvent.getMillisUntilFinished()));
        titleTimer.setText(time);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
        EventBus.getDefault().unregister(this);
    }

    // checkBox状态监听
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked){
            myBinder.startCountDown();
        } else {

        }
    }
}
