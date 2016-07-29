package com.example.dllo.tomatotodo.potatolist.data;

/**
 * Created by dllo on 16/7/27.
 */
public class PotatolistGroupData {
    private int month;
    private int days;
    private int count;
    private int weeks;

    public boolean isThisDay(int month, int days, int weeks) {
        if (month == this.month && days == this.days && weeks == this.weeks) {
            return true;
        }
        return false;
    }


    public int getWeeks() {
        return weeks;
    }

    public PotatolistGroupData setWeeks(int weeks) {
        this.weeks = weeks;
        return this;
    }

    public int getMonth() {
        return month;
    }

    public PotatolistGroupData setMonth(int month) {
        this.month = month;
        return this;
    }

    public int getDays() {
        return days;
    }

    public PotatolistGroupData setDays(int days) {
        this.days = days;
        return this;
    }

    public int getCount() {
        return count;
    }

    public PotatolistGroupData setCount() {
        count ++;
        return this;
    }
}
