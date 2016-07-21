package com.example.dllo.tomatotodo.history;

import java.util.ArrayList;

/**
 * Created by dllo on 16/7/19.
 */
public class HistoryBean {

    private String historyData, historyWeeks, historyNumber, historyFirstTime, historyLastTime, historyName;
    private String type;

    public HistoryBean() {
    }

    public HistoryBean(String historyData, String historyWeeks, String historyNumber) {
        this.historyData = historyData;
        this.historyWeeks = historyWeeks;
        this.historyNumber = historyNumber;
        this.type = "0";
    }

    public HistoryBean(String historyFirstTime, String historyLastTime, String historyName, String type) {
        this.historyFirstTime = historyFirstTime;
        this.historyLastTime = historyLastTime;
        this.historyName = historyName;
        this.type = type;

    }

    public String getHistoryData() {
        return historyData;
    }

    public void setHistoryData(String historyData) {
        this.historyData = historyData;
    }

    public String getHistoryWeeks() {
        return historyWeeks;
    }

    public void setHistoryWeeks(String historyWeeks) {
        this.historyWeeks = historyWeeks;
    }

    public String getHistoryNumber() {
        return historyNumber;
    }

    public void setHistoryNumber(String historyNumber) {
        this.historyNumber = historyNumber;
    }

    public String getHistoryFirstTime() {
        return historyFirstTime;
    }

    public void setHistoryFirstTime(String historyFirstTime) {
        this.historyFirstTime = historyFirstTime;
    }

    public String getHistoryLastTime() {
        return historyLastTime;
    }

    public void setHistoryLastTime(String historyLastTime) {
        this.historyLastTime = historyLastTime;
    }

    public String getHistoryName() {
        return historyName;
    }

    public void setHistoryName(String historyName) {
        this.historyName = historyName;
    }

    public String getType() {
        return type;
    }

    public void setType (String type) {
        this.type = type;
    }
}
