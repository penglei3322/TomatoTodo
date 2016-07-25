package com.example.dllo.tomatotodo.db;

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by zly on 16/7/23.
 */
public class HistoryAllBean {
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;

    private long startTime;
    private long endTime;
    private String tomatoMsg;

    public HistoryAllBean() {
    }

    public HistoryAllBean(long startTime, long endTime, String tomatoMsg) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.tomatoMsg = tomatoMsg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getTomatoMsg() {
        return tomatoMsg;
    }

    public void setTomatoMsg(String tomatoMsg) {
        this.tomatoMsg = tomatoMsg;
    }
}
