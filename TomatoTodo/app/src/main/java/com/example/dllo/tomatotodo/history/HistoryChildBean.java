package com.example.dllo.tomatotodo.history;

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by dllo on 16/8/1.
 */
public class HistoryChildBean {

    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;
    private String firstTime;
    private String lastTime;
    private String name;

    public String getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(String firstTime) {
        this.firstTime = firstTime;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public HistoryChildBean(String firstTime, String lastTime, String name) {
        this.firstTime = firstTime;
        this.lastTime = lastTime;
        this.name = name;
    }

    public HistoryChildBean() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
