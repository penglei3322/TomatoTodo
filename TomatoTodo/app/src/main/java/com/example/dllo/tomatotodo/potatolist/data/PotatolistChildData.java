package com.example.dllo.tomatotodo.potatolist.data;

/**
 * Created by dllo on 16/7/27.
 */
public class PotatolistChildData {

    private String content;
    private int hour;
    private int minute;
    private int lastHour, lastMinute;

    public int getLastHour() {
        return lastHour;
    }

    public void setLastHour(int lastHour) {
        this.lastHour = lastHour;
    }

    public int getLastMinute() {
        return lastMinute;
    }

    public void setLastMinute(int lastMinute) {
        this.lastMinute = lastMinute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
