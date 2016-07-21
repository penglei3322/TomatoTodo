package com.example.dllo.tomatotodo.countdowndetail;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.dllo.tomatotodo.R;
import com.example.dllo.tomatotodo.base.BaseActivity;
import com.example.dllo.tomatotodo.service.CountDownEvent;
import com.example.dllo.tomatotodo.service.TomatoService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zly on 16/7/20.
 */
public class CountdownDetailActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private ProgressView progressView;
    private TextView timeTv;
    private CheckBox startCb;
    private boolean isShowing;

    private ServiceConnection serviceConnection;
    private TomatoService.MyBinder myBinder;

    @Override
    public int initView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_countdown_detail;
    }

    @Override
    public void initData() {
        progressView = (ProgressView) findViewById(R.id.coutdown_detail_progress_view);
        timeTv = (TextView) findViewById(R.id.countdown_detail_time_tv);
        startCb = (CheckBox) findViewById(R.id.countdown_detail_play);

        startCb.setOnCheckedChangeListener(this);


        // 绑定服务
        Intent serviceIntent = new Intent(this, TomatoService.class);
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



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
        EventBus.getDefault().unregister(this);
    }


    @Subscribe
    public void setProgress(CountDownEvent countDownEvent){
        SharedPreferences sharedPreferences = getSharedPreferences("titleTime",MODE_PRIVATE);
        int time = sharedPreferences.getInt("workTime", 25);
        float seconds = time * 60 - countDownEvent.getMillisUntilFinished() / 1000;
        float progress = 360 * seconds / (time * 60);
        progressView.setProgress(progress);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        timeTv.setText(simpleDateFormat.format(new Date(countDownEvent.getMillisUntilFinished())));
    }

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
                timeTv.setText(workTime + ":00");
                isShowing = true;
                startCb.setChecked(false);
                isShowing = false;
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }
}
