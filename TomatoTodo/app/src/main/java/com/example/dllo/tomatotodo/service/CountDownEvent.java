package com.example.dllo.tomatotodo.service;

/**
 * Created by zly on 16/7/18.
 */
public class CountDownEvent {
    private long millisUntilFinished;

    public CountDownEvent(long millisUntilFinished) {
        this.millisUntilFinished = millisUntilFinished;
    }

    public void setMillisUntilFinished(long millisUntilFinished) {
        this.millisUntilFinished = millisUntilFinished;
    }

    public long getMillisUntilFinished() {
        return millisUntilFinished;
    }
}
