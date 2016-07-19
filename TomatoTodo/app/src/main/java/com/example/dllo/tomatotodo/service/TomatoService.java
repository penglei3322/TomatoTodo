package com.example.dllo.tomatotodo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;

/**
 * Created by zly on 16/7/18.
 */
public class TomatoService extends Service {
    private MyBinder myBinder = new MyBinder();
    private int duration = 25;
    private CountDownTimer countDownTimer;


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
    }

    public void countDown(){
//        int duration = myBinder.getDuration();
        countDownTimer = new CountDownTimer(25 * 60 * 1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                EventBus.getDefault().post(new CountDownEvent(millisUntilFinished));
            }

            @Override
            public void onFinish() {

            }
        }.start();

    }


}
