package com.example.dllo.tomatotodo.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.dllo.tomatotodo.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

/**
 * Created by zly on 16/7/18.
 */
public class TomatoService extends Service {
    private MyBinder myBinder = new MyBinder();
    private CountDownTimer countDownTimer;
    private NotificationManager notificationManager;
    private boolean isTick;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }


    public class MyBinder extends Binder{

        public void startCountDown(){
            setTick(true);
            countDown();
        }

        public void cancelCountDown(){
            setTick(false);
            countDownTimer.cancel();
        }

        public boolean isTick(){
            return isTick;
        }
        public void setTick(boolean mTick){
            isTick = mTick;
        }

    }

    public void countDown(){
        SharedPreferences sharedPreferences = getSharedPreferences("titleTime", MODE_PRIVATE);
        int duration = sharedPreferences.getInt("workTime", 25);
        countDownTimer = new CountDownTimer(duration * 60 * 1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                EventBus.getDefault().post(new CountDownEvent(millisUntilFinished));
                showNotification(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                EventBus.getDefault().post(new CountDownEvent(-1));
                myBinder.setTick(false);
            }
        }.start();

    }


    // 显示通知栏
    public void showNotification(long countDownTime){
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_remoteview);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        String time = simpleDateFormat.format(new Date(countDownTime));
        remoteViews.setTextViewText(R.id.notification_countdown_tv, time);
        remoteViews.setTextColor(R.id.notification_countdown_tv, Color.BLACK);
        remoteViews.setTextViewText(R.id.notification_msg_tv, "番茄进行中");
        remoteViews.setTextColor(R.id.notification_msg_tv, Color.BLACK);



        builder.setContent(remoteViews);
//        builder.setOngoing(true);
        Notification notification = builder.build();
        notificationManager.notify(101, notification);
    }

}
