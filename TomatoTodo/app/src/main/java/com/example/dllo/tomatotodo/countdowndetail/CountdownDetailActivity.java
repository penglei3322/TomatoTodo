package com.example.dllo.tomatotodo.countdowndetail;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dllo.tomatotodo.R;
import com.example.dllo.tomatotodo.base.BaseActivity;
import com.example.dllo.tomatotodo.service.CompleteTimerActivity;
import com.example.dllo.tomatotodo.service.CountDownEvent;
import com.example.dllo.tomatotodo.service.TomatoService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zly on 16/7/20.
 */
public class CountdownDetailActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private ProgressView progressView;
    private TextView timeTv;
    private CheckBox startCb;
    private boolean isShowing;
    private ImageView acceptBtn;
    private TextView timeMsg;

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
        acceptBtn = (ImageView) findViewById(R.id.countdown_detail_accept_iv);
        timeMsg = (TextView) findViewById(R.id.countdown_detail_time_msg);

        startCb.setOnCheckedChangeListener(this);
        acceptBtn.setOnClickListener(this);


        // 绑定服务
        Intent serviceIntent = new Intent(this, TomatoService.class);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                myBinder = (TomatoService.MyBinder) service;
                if (myBinder.isTick()){
                    startCb.setChecked(true);
                } else {
                    if (myBinder.isFinish()) {
                        timeTv.setText("番茄已完成");
                        timeMsg.setText("点击以提交");
                        startCb.setVisibility(View.GONE);
                        acceptBtn.setVisibility(View.VISIBLE);
                        progressView.setProgress(360);
                    }
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
        if (countDownEvent.getMillisUntilFinished() > 0) {
            progressView.setProgress(progress);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
//            if (myBinder.isRest()){
//                timeTv.setTextColor(Color.GREEN);
//                acceptBtn.setVisibility(View.GONE);
//                startCb.setVisibility(View.VISIBLE);
//            } else {
//                timeTv.setTextColor(Color.BLACK);
//            }
            timeTv.setText(simpleDateFormat.format(new Date(countDownEvent.getMillisUntilFinished())));
        } else if (countDownEvent.getMillisUntilFinished() == -1){ // 工作结束
            timeTv.setText("番茄已完成");
            timeMsg.setText("点击以提交");
            startCb.setVisibility(View.GONE);
            acceptBtn.setVisibility(View.VISIBLE);
        }
//        if (countDownEvent.getMillisUntilFinished() == 0){ // 休息结束
//            SharedPreferences preferences = getSharedPreferences("titleTime", MODE_PRIVATE);
//            int workTime = preferences.getInt("workTime", 25);
//            timeTv.setText(workTime + ":00");
//            timeTv.setTextColor(Color.BLACK);
//            isShowing = true;
//            startCb.setChecked(false);
//            isShowing = false;
//        }
    }

    public void initProgress(){

    }

    // checkBox状态监听
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (!myBinder.isTick()) {
                myBinder.startCountDown();
            }
        } else {
            if (!isShowing && !myBinder.isRest()){
                showDelAlert();
                startCb.setChecked(true);
            } else {
                // 取消休息
//                myBinder.cancelCountDown();
//                SharedPreferences sharedPreferences = getSharedPreferences("titleTime", MODE_PRIVATE);
//                int workTime = sharedPreferences.getInt("workTime", 25);
//                timeTv.setText(workTime + ":00");
//                timeTv.setTextColor(Color.BLACK);
//                isShowing = true;
//                startCb.setChecked(false);
//                isShowing = false;
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
                progressView.setProgress(0);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }


    // accept监听
    @Override
    public void onClick(View v) {
        Intent intentComplete = new Intent(CountdownDetailActivity.this, CompleteTimerActivity.class);
        startActivity(intentComplete);
    }
}
