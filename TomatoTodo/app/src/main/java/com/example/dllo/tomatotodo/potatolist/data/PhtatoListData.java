package com.example.dllo.tomatotodo.potatolist.data;

import com.litesuits.orm.db.annotation.NotNull;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;

import java.util.Date;

/**
 * Created by dllo on 16/7/20.
 */

public class PhtatoListData implements Comparable<PhtatoListData> {
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;

    @NotNull
    private String content;
    private int pos;
    private boolean Checked = false;
    private boolean topCheck = false;
    private int month;
    private int day;
    private int hour,minute,weeks;
    private String describe;

    private long getData(){
        Date date = new Date(2016,month,day,hour,minute);
        return date.getTime();
    }

    public int getWeeks() {
        return weeks;
    }

    public void setWeeks(int weeks) {
        this.weeks = weeks;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
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

    public boolean isTopCheck() {
        return topCheck;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public void setTopCheck(boolean topCheck) {
        this.topCheck = topCheck;
    }

    public boolean isItemChecked() {
        return Checked;
    }

    public void setIsItemChecked(boolean isItemChecked) {
        this.Checked = isItemChecked;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    /**
     * Compares this object to the specified object to determine their relative
     * order.
     *
     * @param another the object to compare to this instance.
     * @return a negative integer if this instance is less than {@code another};
     * a positive integer if this instance is greater than
     * {@code another}; 0 if this instance has the same order as
     * {@code another}.
     * @throws ClassCastException if {@code another} cannot be converted into something
     *                            comparable to {@code this} instance.
     */
    @Override
    public int compareTo(PhtatoListData another) {
        if(another.isTopCheck() == isTopCheck()){
            return 0;
        }
        if(another.isTopCheck()){
            return 1;
        }
        return -1;
    }
}
