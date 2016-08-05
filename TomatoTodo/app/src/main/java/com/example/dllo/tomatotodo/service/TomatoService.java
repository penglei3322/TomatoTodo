package com.example.dllo.tomatotodo.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.SoundPool;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.example.dllo.tomatotodo.R;
import com.example.dllo.tomatotodo.countdowndetail.CompleteTimerActivity;
import com.example.dllo.tomatotodo.countdowndetail.CountdownDetailActivity;
import com.example.dllo.tomatotodo.main.MainActivity;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by zly on 16/7/18.
 */
public class TomatoService extends Service {
    private MyBinder myBinder = new MyBinder();
    private CountDownTimer countDownTimer;
    private NotificationManager notificationManager;

    private SoundPool soundPool;
    private HashMap<String, Integer> soundId;

    private long startWorkTime;

    private boolean isTick;
    private boolean isWorkFinish;
    private boolean isRest;

    private HashMap<String, Class> intentClass;

    @Override
    public void onCreate() {
        super.onCreate();

        intentClass = new HashMap<>();
        intentClass.put("working", CountdownDetailActivity.class);
        intentClass.put("finish", CompleteTimerActivity.class);
        intentClass.put("resting", MainActivity.class);

        soundId = new HashMap<>();
        soundPool = new SoundPool(2, 0, 5);
        soundId.put("working", soundPool.load(this, R.raw.tick, 1));
        soundId.put("finished", soundPool.load(this, R.raw.alertmusic, 1));
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
            playTick();
        }

        public void startRestCountDown() {
            isRest = true;
            setTick(true);
            SharedPreferences sharedPreferences = getSharedPreferences("titleTime", MODE_PRIVATE);
            int duration = sharedPreferences.getInt("restTime", 5);
            countDown(duration);
        }

        public void cancelCountDown() {
            setTick(false);
            countDownTimer.cancel();
            if (isRest) {
                isRest = false;
            }
            stopForeground(true);
            stopTick();
        }

        public boolean isTick() {
            return isTick;
        }

        public void setTick(boolean mTick) {
            isTick = mTick;
        }

        public boolean isWorkFinish() {
            return isWorkFinish;
        }


        public boolean isRest() {
            return isRest;
        }

        public long getStartWorkTime() {
            return startWorkTime;
        }

    }

    // 倒计时
    public void countDown(int duration) {
        countDownTimer = new CountDownTimer(duration * 60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                EventBus.getDefault().post(new CountDownEvent(millisUntilFinished));
                showNotification(millisUntilFinished);
                isWorkFinish = false;
            }

            @Override
            public void onFinish() {
                myBinder.setTick(false);
                if (isRest) {
                    isRest = false;
                    EventBus.getDefault().post(new CountDownEvent(0)); // 休息结束
                    showNotification(0);
                } else {
                    isWorkFinish = true;
                    EventBus.getDefault().post(new CountDownEvent(-1));// 工作结束
                    showNotification(-1);
                    stopTick();
                    playAlert();

                }
            }
        }.start();

    }


    // 显示通知栏
    public void showNotification(long countDownTime) {

        Class cls = null;

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_remoteview);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        String time = simpleDateFormat.format(new Date(countDownTime));
        if (countDownTime > 0) {
            remoteViews.setTextViewText(R.id.notification_countdown_tv, time);
            remoteViews.setTextViewText(R.id.notification_msg_tv, "番茄进行中");
            cls = intentClass.get("working");
        } else if (countDownTime == -1) {
            remoteViews.setTextViewText(R.id.notification_countdown_tv, "番茄时间结束");
            remoteViews.setTextViewText(R.id.notification_msg_tv, "点击以提交番茄");
            cls = intentClass.get("finish");
        } else {
            remoteViews.setTextViewText(R.id.notification_countdown_tv, "休息结束");
            remoteViews.setTextViewText(R.id.notification_msg_tv, "点击返回");
            cls = intentClass.get("resting");
        }

        if (isRest) {
            remoteViews.setTextViewText(R.id.notification_msg_tv, "休息中");
            cls = intentClass.get("resting");
        }

        Intent intent = new Intent(this, cls);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notification_layout, pendingIntent);

        builder.setContent(remoteViews);
//        builder.setOngoing(true);
        Notification notification = builder.build();
        startForeground(101, notification);
//        notificationManager.notify(101, notification);
    }

    // 播放滴答声
    public void playTick() {
        soundPool.play(soundId.get("working"), 1, 1, 1, -1, 1);
    }

    // 停止播放滴答声
    public void stopTick() {
        soundPool.stop(soundId.get("working"));
    }

    // 播放闹铃
    public void playAlert() {
        soundPool.play(soundId.get("finished"), 1, 1, 1, 0, 1);
    }

    // 停止闹铃
    public void stopAlert() {
        soundPool.stop(soundId.get("finished"));
    }

}
