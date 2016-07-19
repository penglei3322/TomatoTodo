package com.example.dllo.tomatotodo.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
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
    private int duration = 25;
    private CountDownTimer countDownTimer;
    private NotificationManager notificationManager;


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
        public void setDuration(int mDuration){
            duration = mDuration;
        }
        public int getDuration(){
            return duration;
        }

        public void startCountDown(){
            countDown();
        }

        public void cancelCountDown(){
            countDownTimer.cancel();
        }
    }

    public void countDown(){
//        int duration = myBinder.getDuration();
        countDownTimer = new CountDownTimer(1 * 60 * 1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                EventBus.getDefault().post(new CountDownEvent(millisUntilFinished));
                showNotification(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                EventBus.getDefault().post(new CountDownEvent(-1));
            }
        }.start();

    }


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
