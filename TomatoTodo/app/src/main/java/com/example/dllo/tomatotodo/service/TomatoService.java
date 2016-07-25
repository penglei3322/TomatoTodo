package com.example.dllo.tomatotodo.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import com.litesuits.orm.LiteOrm;

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

    private long startWorkTime;

    private boolean isTick;
    private boolean isWorkFinish;
    private boolean isRest;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }


    public class MyBinder extends Binder {

        public void startCountDown() {
            startWorkTime = System.currentTimeMillis();
            setTick(true);
            SharedPreferences sharedPreferences = getSharedPreferences("titleTime", MODE_PRIVATE);
            int duration = sharedPreferences.getInt("workTime", 25);
            countDown(duration);
        }

        public void startRestCountDown(){
            isRest = true;
            setTick(true);
            SharedPreferences sharedPreferences = getSharedPreferences("titleTime", MODE_PRIVATE);
            int duration = sharedPreferences.getInt("restTime", 5);
            countDown(duration);
        }

        public void cancelCountDown() {
            setTick(false);
            countDownTimer.cancel();
            if (isRest){
                isRest = false;
            }
            stopForeground(true);
        }

        public boolean isTick() {
            return isTick;
        }

        public void setTick(boolean mTick) {
            isTick = mTick;
        }

        public boolean isWorkFinish(){
            return isWorkFinish;
        }



        public boolean isRest(){
            return isRest;
        }

        public long getStartWorkTime(){
            return startWorkTime;
        }

    }

    // 倒计时
    public void countDown(int duration) {
        countDownTimer = new CountDownTimer(1 * 10 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                EventBus.getDefault().post(new CountDownEvent(millisUntilFinished));
                showNotification(millisUntilFinished);
                isWorkFinish = false;
            }

            @Override
            public void onFinish() {
                myBinder.setTick(false);
                if (isRest){
                    isRest = false;
                    EventBus.getDefault().post(new CountDownEvent(0)); // 休息结束
                    showNotification(0);
                } else {
                    isWorkFinish = true;
                    EventBus.getDefault().post(new CountDownEvent(-1));// 工作结束
                    showNotification(-1);
                }
            }
        }.start();

    }



    // 显示通知栏
    public void showNotification(long countDownTime) {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_remoteview);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        String time = simpleDateFormat.format(new Date(countDownTime));
        if (countDownTime > 0) {
            remoteViews.setTextViewText(R.id.notification_countdown_tv, time);
            remoteViews.setTextViewText(R.id.notification_msg_tv, "番茄进行中");
        } else if (countDownTime == -1){
            remoteViews.setTextViewText(R.id.notification_countdown_tv, "番茄时间结束");
            remoteViews.setTextViewText(R.id.notification_msg_tv, "点击以提交番茄");
        } else {
            remoteViews.setTextViewText(R.id.notification_countdown_tv, "休息结束");
            remoteViews.setTextViewText(R.id.notification_msg_tv, "点击返回");
        }

        if (isRest){
            remoteViews.setTextViewText(R.id.notification_msg_tv, "休息中");
        }

        Intent intent = new Intent(this, CompleteTimerActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,1,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notification_layout,pendingIntent);

        builder.setContent(remoteViews);
//        builder.setOngoing(true);
        Notification notification = builder.build();
        startForeground(101, notification);
//        notificationManager.notify(101, notification);
    }


}
