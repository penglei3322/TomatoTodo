package com.example.dllo.tomatotodo.history;

/**
 * Created by dllo on 16/8/1.
 */
public class HistoryGroupBean {

    private int data;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public HistoryGroupBean(int data) {
        this.data = data;
    }

    public HistoryGroupBean() {
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

}
