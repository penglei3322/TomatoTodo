package com.example.dllo.tomatotodo.main;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import android.widget.TextView;

import com.example.dllo.tomatotodo.R;
import com.example.dllo.tomatotodo.base.BaseActivity;
import com.example.dllo.tomatotodo.countdowndetail.CountdownDetailActivity;
import com.example.dllo.tomatotodo.history.HistoryFragment;
import com.example.dllo.tomatotodo.potatolist.PotatoListFragment;
import com.example.dllo.tomatotodo.service.CountDownEvent;
import com.example.dllo.tomatotodo.service.TomatoService;
import com.example.dllo.tomatotodo.statistics.StatisticsFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private TabLayout tabLayout;
    private MainAdapter adapter;
    private ArrayList<Fragment> fragments;
    private ViewPager viewPager;
    private TextView titleTimer;
    private CheckBox startCb;
    private ServiceConnection serviceConnection;
    private TomatoService.MyBinder myBinder;
    private NotificationManager notificationManager;
    private boolean isShowing = false;


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
        fragments.add(new HistoryFragment());
        fragments.add(new StatisticsFragment());
        adapter.setFragments(fragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        titleTimer.setOnClickListener(this);

        // 绑定服务
        Intent serviceIntent = new Intent(this, TomatoService.class);
        startService(serviceIntent);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                myBinder = (TomatoService.MyBinder) service;
                if (myBinder.isTick()){
                    startCb.setChecked(true);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE);

        startCb.setOnCheckedChangeListener(this);

        SharedPreferences sharedPreferences = getSharedPreferences("titleTime",MODE_PRIVATE);
        int workTime = sharedPreferences.getInt("workTime",25);
        titleTimer.setText(workTime + ":00");



    }

    @Subscribe
    public void setTitleTimer(CountDownEvent countDownEvent) {
        if (countDownEvent.getMillisUntilFinished() == -1) {
            titleTimer.setText("番茄已完成");
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
            String time = simpleDateFormat.format(new Date(countDownEvent.getMillisUntilFinished()));
            titleTimer.setText(time);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
//        if (myBinder.isTick()){
//            startCb.setChecked(true);
//        }
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
        if (isChecked) {
            if (!myBinder.isTick()) {
                myBinder.startCountDown();
            }
        } else {
            if (isShowing == false){
                showDelAlert();
                startCb.setChecked(true);
            }
        }
    }


    // 显示dialog
    public void showDelAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("放弃番茄");
        builder.setMessage("真的要放弃这个番茄吗?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myBinder.cancelCountDown();
                SharedPreferences sharedPreferences = getSharedPreferences("titleTime",MODE_PRIVATE);
                int workTime = sharedPreferences.getInt("workTime",25);
                titleTimer.setText(workTime + ":00");
                isShowing = true;
                startCb.setChecked(false);
                isShowing = false;
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_timer:
                Intent intent = new Intent(MainActivity.this, CountdownDetailActivity.class);
                startActivity(intent);
                break;
        }
    }
}
